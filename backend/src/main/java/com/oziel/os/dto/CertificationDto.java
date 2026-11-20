package com.oziel.os.dto;

import com.oziel.os.entity.CertificationCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CertificationDto(
    Long id,
    @NotBlank @Size(max = 200) String name,
    @NotBlank @Size(max = 200) String issuer,
    @NotNull CertificationCategory category,
    @NotNull LocalDate awardedOn,
    LocalDate expiresOn,
    @Size(max = 2048) @Pattern(regexp = "^https?://\\S+$", message = "Credential URL must use http or https")
    String credentialUrl,
    @Size(max = 255) String credentialId,
    boolean publiclyVisible
) {}
