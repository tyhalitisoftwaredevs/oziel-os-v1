package com.oziel.os.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="build_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BuildLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String title;
    @Column(columnDefinition="TEXT") private String content;
    @Enumerated(EnumType.STRING) private LogCategory category;
    private String tags;
    @Builder.Default private Instant createdAt=Instant.now();
    private String imageUrl;
}
