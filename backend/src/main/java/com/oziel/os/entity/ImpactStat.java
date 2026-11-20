package com.oziel.os.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="impact_stats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ImpactStat {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String label;
    @Column(name="stat_value")
    private Integer value;
    private String description;
}
