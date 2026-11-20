package com.oziel.os.service;

import com.oziel.os.entity.*;
import com.oziel.os.repository.OpportunityAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpportunityAlertServiceTest {
    @Mock OpportunityAlertRepository repository;
    @Mock JavaMailSender mailSender;
    @InjectMocks OpportunityAlertService service;

    @Test void enqueueCreatesEmailOnlyWhenSmsIsDisabled() {
        ReflectionTestUtils.setField(service,"emailEnabled",true);
        ReflectionTestUtils.setField(service,"emailTo","asenath@example.com");
        ReflectionTestUtils.setField(service,"smsEnabled",false);
        var opportunity=Opportunity.builder().id(4L).name("Java Developer Intern").company("Example Bank")
            .type(OpportunityType.INTERNSHIP).link("https://example.com/careers").build();

        service.enqueue(opportunity);

        var alert=ArgumentCaptor.forClass(OpportunityAlert.class);
        verify(repository,times(1)).save(alert.capture());
        assertEquals(NotificationChannel.EMAIL,alert.getValue().getChannel());
        assertEquals("asenath@example.com",alert.getValue().getRecipient());
    }
}
