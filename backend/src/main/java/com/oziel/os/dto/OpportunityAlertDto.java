package com.oziel.os.dto;
import com.oziel.os.entity.DeliveryStatus;
import com.oziel.os.entity.NotificationChannel;
import java.time.Instant;
public record OpportunityAlertDto(Long id, String opportunity, String company, NotificationChannel channel,
                                  String recipient, DeliveryStatus status, int attempts, String lastError,
                                  Instant createdAt, Instant sentAt) {}
