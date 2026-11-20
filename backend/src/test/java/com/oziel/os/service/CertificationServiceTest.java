package com.oziel.os.service;

import com.oziel.os.dto.CertificationDto;
import com.oziel.os.entity.Certification;
import com.oziel.os.entity.CertificationCategory;
import com.oziel.os.exception.ResourceNotFoundException;
import com.oziel.os.repository.CertificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificationServiceTest {
    @Mock CertificationRepository repository;
    @InjectMocks CertificationService service;

    @Test
    void publicListUsesOnlyPublishedRecords() {
        when(repository.findByPubliclyVisibleTrueOrderByAwardedOnDesc()).thenReturn(List.of(certification(1L, true)));

        var result = service.findPublic();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().publiclyVisible());
        verify(repository).findByPubliclyVisibleTrueOrderByAwardedOnDesc();
    }

    @Test
    void createSavesCredentialAndVisibilityChoice() {
        when(repository.save(any(Certification.class))).thenAnswer(invocation -> {
            Certification saved = invocation.getArgument(0);
            saved.setId(7L);
            return saved;
        });

        var result = service.create(dto(true));

        assertEquals(7L, result.id());
        assertTrue(result.publiclyVisible());
        assertEquals("Java Foundations", result.name());
    }

    @Test
    void updateThrowsWhenCredentialDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, dto(false)));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteThrowsWhenCredentialDoesNotExist() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(repository, never()).deleteById(any());
    }

    private Certification certification(Long id, boolean publiclyVisible) {
        return Certification.builder()
            .id(id).name("Java Foundations").issuer("Example Institute")
            .category(CertificationCategory.JAVA).awardedOn(LocalDate.of(2026, 1, 10))
            .publiclyVisible(publiclyVisible).build();
    }

    private CertificationDto dto(boolean publiclyVisible) {
        return new CertificationDto(
            null, "Java Foundations", "Example Institute", CertificationCategory.JAVA,
            LocalDate.of(2026, 1, 10), null, null, null, publiclyVisible
        );
    }
}
