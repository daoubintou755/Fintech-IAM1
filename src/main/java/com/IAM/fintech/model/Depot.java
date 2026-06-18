package com.IAM.fintech.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Depot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer nbMises;
    private Double montant;
    private LocalDateTime date;
    @ManyToOne private Client client;
    @ManyToOne
    private Cycle cycle;
}