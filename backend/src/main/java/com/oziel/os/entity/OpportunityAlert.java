package com.oziel.os.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="opportunity_alerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OpportunityAlert {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="opportunity_id", nullable=false)
    private Opportunity opportunity;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private NotificationChannel channel;
    @Column(nullable=false) private String recipient;
    @Column(columnDefinition="TEXT", nullable=false) private String message;
    @Enumerated(EnumType.STRING) @Column(nullable=false) @Builder.Default private DeliveryStatus status=DeliveryStatus.PENDING;
    @Builder.Default private int attempts=0;
    @Column(columnDefinition="TEXT") private String lastError;
    @Builder.Default private Instant createdAt=Instant.now();
    private Instant sentAt;
    @Builder.Default private Instant nextAttemptAt=Instant.now();
}
