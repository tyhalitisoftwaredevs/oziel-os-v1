package com.oziel.os.controller;

import com.oziel.os.dto.CertificationDto;
import com.oziel.os.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/certifications")
@RequiredArgsConstructor
public class AdminCertificationController {
    private final CertificationService service;

    @GetMapping
    public List<CertificationDto> all() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<CertificationDto> create(@Valid @RequestBody CertificationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public CertificationDto update(@PathVariable Long id, @Valid @RequestBody CertificationDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
