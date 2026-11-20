package com.oziel.os.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name="opportunities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Opportunity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String name;
    private String company;
    @Enumerated(EnumType.STRING) private OpportunityType type;
    private LocalDate closingDate;
    @Enumerated(EnumType.STRING) private OpportunityStatus status;
    private String link;
}
