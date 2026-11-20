package com.oziel.os.service;
import com.oziel.os.dto.ProjectDto;
import com.oziel.os.entity.Project;
import com.oziel.os.entity.ProjectStatus;
import com.oziel.os.exception.ResourceNotFoundException;
import com.oziel.os.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;
    public List<ProjectDto> findAll(ProjectStatus status, Boolean featured) {
        List<Project> projects = status != null ? repository.findByStatus(status) :
            Boolean.TRUE.equals(featured) ? repository.findByFeaturedTrue() : repository.findAll();
        return projects.stream().map(this::toDto).toList();
    }
    public ProjectDto findBySlug(String slug) {
        return repository.findBySlug(slug).map(this::toDto).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }
    @Transactional
    public ProjectDto create(ProjectDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }
    @Transactional
    public ProjectDto update(Long id, ProjectDto dto) {
        Project existing=repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        existing.setTitle(dto.title()); existing.setSlug(dto.slug()); existing.setProblem(dto.problem());
        existing.setSolution(dto.solution()); existing.setTechStack(dto.techStack()); existing.setStatus(dto.status());
        existing.setDemoUrl(dto.demoUrl()); existing.setGithubUrl(dto.githubUrl()); existing.setImageUrl(dto.imageUrl());
        existing.setImpact(dto.impact()); existing.setFeatured(dto.featured());
        return toDto(repository.save(existing));
    }
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Project not found");
        repository.deleteById(id);
    }
    private Project toEntity(ProjectDto dto) {
        return Project.builder().title(dto.title()).slug(dto.slug()).problem(dto.problem()).solution(dto.solution())
            .techStack(dto.techStack()==null ? List.of() : dto.techStack()).status(dto.status()).demoUrl(dto.demoUrl())
            .githubUrl(dto.githubUrl()).imageUrl(dto.imageUrl()).impact(dto.impact()).featured(dto.featured()).build();
    }
    private ProjectDto toDto(Project p) {
        return new ProjectDto(p.getId(),p.getTitle(),p.getSlug(),p.getProblem(),p.getSolution(),p.getTechStack(),
            p.getStatus(),p.getDemoUrl(),p.getGithubUrl(),p.getImageUrl(),p.getImpact(),p.isFeatured(),p.getCreatedAt());
    }
}
