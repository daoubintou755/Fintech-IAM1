package com.IAM.fintech.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Retenue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Double amount;
    private String type;
    private LocalDateTime date;
    @ManyToOne private Client client;
}