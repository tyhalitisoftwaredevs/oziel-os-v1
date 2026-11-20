package com.oziel.os.service;

import com.oziel.os.dto.OpportunityAlertDto;
import com.oziel.os.entity.*;
import com.oziel.os.repository.OpportunityAlertRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service @RequiredArgsConstructor
public class OpportunityAlertService {
    private static final Logger log=LoggerFactory.getLogger(OpportunityAlertService.class);
    private final OpportunityAlertRepository repository;
    private final JavaMailSender mailSender;
    private final RestClient restClient=RestClient.create();

    @Value("${notifications.email.to:}") private String emailTo;
    @Value("${notifications.email.enabled:true}") private boolean emailEnabled;
    @Value("${spring.mail.host:}") private String smtpHost;
    @Value("${spring.mail.username:}") private String smtpUsername;
    @Value("${spring.mail.password:}") private String smtpPassword;
    @Value("${spring.mail.properties.mail.smtp.auth:true}") private boolean smtpAuth;
    @Value("${notifications.email.from:}") private String emailFrom;
    @Value("${notifications.sms.to:}") private String smsTo;
    @Value("${notifications.sms.enabled:false}") private boolean smsEnabled;
    @Value("${notifications.twilio.account-sid:}") private String accountSid;
    @Value("${notifications.twilio.auth-token:}") private String authToken;
    @Value("${notifications.twilio.from:}") private String smsFrom;
    @Value("${notifications.max-attempts:8}") private int maxAttempts;

    @Transactional
    public void enqueue(Opportunity opportunity) {
        String message="New OZIEL OS opportunity: "+opportunity.getName()+" at "+opportunity.getCompany()
            +". Type: "+opportunity.getType()+". Apply/check details: "+opportunity.getLink();
        if(emailEnabled) repository.save(alert(opportunity,NotificationChannel.EMAIL,emailTo,message));
        if(smsEnabled) repository.save(alert(opportunity,NotificationChannel.SMS,smsTo,message));
    }

    @Scheduled(fixedDelayString="${notifications.poll-interval-ms:15000}")
    public void dispatchPending() {
        var due=repository.findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(DeliveryStatus.PENDING,Instant.now());
        for(var alert:due) {
            if(!configured(alert.getChannel())) {
                alert.setLastError("Delivery is waiting for recipient and provider configuration.");
                alert.setNextAttemptAt(Instant.now().plus(Duration.ofMinutes(5)));
                repository.save(alert);
                log.warn("{} notification {} is queued; configure its recipient and provider to enable delivery.",alert.getChannel(),alert.getId());
                continue;
            }
            try {
                deliver(alert);
                alert.setStatus(DeliveryStatus.SENT);
                alert.setSentAt(Instant.now());
                alert.setLastError(null);
                repository.save(alert);
                log.info("{} notification {} sent for opportunity {}.",alert.getChannel(),alert.getId(),alert.getOpportunity().getId());
            } catch(MailException | RestClientException ex) {
                alert.setAttempts(alert.getAttempts()+1);
                alert.setLastError(ex.getMessage());
                if(alert.getAttempts()>=maxAttempts) {
                    alert.setStatus(DeliveryStatus.FAILED);
                } else {
                    long delayMinutes=Math.min(60,1L << Math.min(alert.getAttempts(),6));
                    alert.setNextAttemptAt(Instant.now().plus(Duration.ofMinutes(delayMinutes)));
                }
                repository.save(alert);
                log.error("{} notification {} failed on attempt {}: {}",alert.getChannel(),alert.getId(),alert.getAttempts(),ex.getMessage());
            }
        }
    }

    @Transactional(readOnly=true)
    public List<OpportunityAlertDto> recent() {
        return repository.findTop50ByOrderByCreatedAtDesc().stream()
            .map(a->new OpportunityAlertDto(a.getId(),a.getOpportunity().getName(),a.getOpportunity().getCompany(),
                a.getChannel(),a.getRecipient(),a.getStatus(),a.getAttempts(),a.getLastError(),a.getCreatedAt(),a.getSentAt()))
            .toList();
    }

    private OpportunityAlert alert(Opportunity opportunity,NotificationChannel channel,String recipient,String message) {
        return OpportunityAlert.builder().opportunity(opportunity).channel(channel)
            .recipient(recipient==null?"":recipient).message(message).build();
    }
    private boolean configured(NotificationChannel channel) {
        return switch(channel) {
            case EMAIL -> emailEnabled && !emailTo.isBlank() && !smtpHost.isBlank()
                && (!smtpAuth || (!smtpUsername.isBlank() && !smtpPassword.isBlank()));
            case SMS -> smsEnabled && !smsTo.isBlank() && !accountSid.isBlank() && !authToken.isBlank() && !smsFrom.isBlank();
        };
    }
    private void deliver(OpportunityAlert alert) {
        if(alert.getChannel()==NotificationChannel.EMAIL) {
            var email=new SimpleMailMessage();
            email.setTo(alert.getRecipient());
            if(!emailFrom.isBlank()) email.setFrom(emailFrom);
            email.setSubject("New tech opportunity: "+alert.getOpportunity().getName());
            email.setText(alert.getMessage());
            mailSender.send(email);
            return;
        }
        var body=new LinkedMultiValueMap<String,String>();
        body.add("To",alert.getRecipient());
        body.add("From",smsFrom);
        body.add("Body",alert.getMessage());
        restClient.post()
            .uri("https://api.twilio.com/2010-04-01/Accounts/{accountSid}/Messages.json",accountSid)
            .headers(headers->headers.setBasicAuth(accountSid,authToken))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}
