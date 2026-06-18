package com.IAM.fintech.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Mouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String type; // ex: "MISE", "RETRAIT", "CREDIT_CLIENT"

    @Column(nullable = false)
    private Double amount;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id") // nullable = true car certaines opérations ne dépendent pas d'un cycle précis
    private Cycle cycle;
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Utilisateur agent; // L'agent qui a effectué l'opération
}