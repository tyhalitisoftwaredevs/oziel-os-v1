package com.oziel.os.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String title;
    @Column(unique=true, nullable=false) @NotBlank private String slug;
    @Column(columnDefinition="TEXT") private String problem;
    @Column(columnDefinition="TEXT") private String solution;
    @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="project_tech", joinColumns=@JoinColumn(name="project_id"))
    @Builder.Default private List<String> techStack = new ArrayList<>();
    @Enumerated(EnumType.STRING) private ProjectStatus status;
    private String demoUrl;
    private String githubUrl;
    private String imageUrl;
    private String impact;
    private boolean featured;
    @Builder.Default private Instant createdAt = Instant.now();
}
