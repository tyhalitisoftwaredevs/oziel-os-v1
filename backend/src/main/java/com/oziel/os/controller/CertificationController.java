package com.oziel.os.controller;

import com.oziel.os.dto.CertificationDto;
import com.oziel.os.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
@RequiredArgsConstructor
public class CertificationController {
    private final CertificationService service;

    @GetMapping
    public List<CertificationDto> publicCertifications() {
        return service.findPublic();
    }
}
