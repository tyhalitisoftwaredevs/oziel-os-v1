package com.oziel.os.service;

import com.oziel.os.dto.CertificationDto;
import com.oziel.os.entity.Certification;
import com.oziel.os.exception.ResourceNotFoundException;
import com.oziel.os.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final CertificationRepository repository;

    public List<CertificationDto> findPublic() {
        return repository.findByPubliclyVisibleTrueOrderByAwardedOnDesc().stream().map(this::toDto).toList();
    }

    public List<CertificationDto> findAll() {
        return repository.findAllByOrderByAwardedOnDesc().stream().map(this::toDto).toList();
    }

    @Transactional
    public CertificationDto create(CertificationDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    @Transactional
    public CertificationDto update(Long id, CertificationDto dto) {
        Certification existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));
        apply(existing, dto);
        return toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Certification not found");
        repository.deleteById(id);
    }

    private Certification toEntity(CertificationDto dto) {
        var certification = new Certification();
        apply(certification, dto);
        return certification;
    }

    private void apply(Certification certification, CertificationDto dto) {
        certification.setName(dto.name());
        certification.setIssuer(dto.issuer());
        certification.setCategory(dto.category());
        certification.setAwardedOn(dto.awardedOn());
        certification.setExpiresOn(dto.expiresOn());
        certification.setCredentialUrl(dto.credentialUrl());
        certification.setCredentialId(dto.credentialId());
        certification.setPubliclyVisible(dto.publiclyVisible());
    }

    private CertificationDto toDto(Certification certification) {
        return new CertificationDto(
            certification.getId(),
            certification.getName(),
            certification.getIssuer(),
            certification.getCategory(),
            certification.getAwardedOn(),
            certification.getExpiresOn(),
            certification.getCredentialUrl(),
            certification.getCredentialId(),
            certification.isPubliclyVisible()
        );
    }
}
