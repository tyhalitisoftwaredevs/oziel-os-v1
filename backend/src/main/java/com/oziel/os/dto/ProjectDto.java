package com.oziel.os.dto;
import com.oziel.os.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
public record ProjectDto(Long id, @NotBlank String title, @NotBlank String slug, String problem, String solution,
                         List<String> techStack, ProjectStatus status, String demoUrl, String githubUrl,
                         String imageUrl, String impact, boolean featured, Instant createdAt) {}
