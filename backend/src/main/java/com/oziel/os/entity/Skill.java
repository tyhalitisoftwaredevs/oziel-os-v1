package com.oziel.os.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max; import jakarta.validation.constraints.Min;
import lombok.*;

@Entity @Table(name="skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String name;
    @Enumerated(EnumType.STRING) private SkillCategory category;
    @Enumerated(EnumType.STRING) private SkillLevel level;
    @Min(0) @Max(100) private Integer confidence;
    private String proofUrl;
}
