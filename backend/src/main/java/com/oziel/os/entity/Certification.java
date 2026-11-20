package com.oziel.os.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String issuer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationCategory category;

    @NotNull
    @Column(nullable = false)
    private LocalDate awardedOn;

    private LocalDate expiresOn;

    @Column(length = 2048)
    private String credentialUrl;

    @Column(length = 255)
    private String credentialId;

    @Builder.Default
    @Column(nullable = false)
    private boolean publiclyVisible = false;
}
