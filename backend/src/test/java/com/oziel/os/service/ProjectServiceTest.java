package com.oziel.os.service;
import com.oziel.os.dto.ProjectDto;
import com.oziel.os.entity.Project;
import com.oziel.os.entity.ProjectStatus;
import com.oziel.os.exception.ResourceNotFoundException;
import com.oziel.os.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock ProjectRepository repository;
    @InjectMocks ProjectService service;
    private Project project() {
        return Project.builder().id(1L).title("Oziel OS").slug("oziel-os").problem("Problem").solution("Solution")
            .techStack(List.of("Java","Spring")).status(ProjectStatus.LIVE).featured(true).build();
    }
    @Test void findAllReturnsDtos() {
        when(repository.findAll()).thenReturn(List.of(project()));
        assertEquals(1,service.findAll(null,null).size());
    }
    @Test void findBySlugReturnsProject() {
        when(repository.findBySlug("oziel-os")).thenReturn(Optional.of(project()));
        assertEquals("Oziel OS",service.findBySlug("oziel-os").title());
    }
    @Test void findBySlugThrowsWhenMissing() {
        when(repository.findBySlug("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->service.findBySlug("missing"));
    }
    @Test void createSavesProject() {
        when(repository.save(any(Project.class))).thenAnswer(inv -> {Project p=inv.getArgument(0);p.setId(1L);return p;});
        var dto=new ProjectDto(null,"Test","test","Problem","Solution",List.of("Java"),ProjectStatus.LIVE,null,null,null,"Impact",true,null);
        assertEquals("test",service.create(dto).slug());
    }
    @Test void deleteThrowsWhenMissing() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->service.delete(99L));
    }
    @Test void updateChangesExistingProject() {
        var existing=project();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        var dto=new ProjectDto(1L,"Updated","updated","New problem","New solution",List.of("React"),ProjectStatus.IN_PROGRESS,null,null,null,"Impact",false,null);
        assertEquals("Updated",service.update(1L,dto).title());
    }
}
