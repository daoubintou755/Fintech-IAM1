package com.IAM.fintech.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true) // Bonne pratique : le code doit être unique
    private String code;

    private Double mise;
    private String status; // Ex: "EN_COURS", "CLOTURE"

    private LocalDateTime dateOuverture;
    private LocalDateTime dateFermeture;

    @ManyToOne
    @JoinColumn(name = "client_id") // Définit explicitement la clé étrangère
    private Client client;

    private int nbCollectes = 0;

    // Ajout pour la traçabilité : un cycle peut avoir plusieurs dépôts
    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL)
    private List<Depot> depots = new ArrayList<>();

    // Constructeur personnalisé conservé
    public Cycle(String nom, Double mise, Client client) {
        this.nom = nom;
        this.mise = mise;
        this.client = client;
        this.status = "EN_COURS";
        this.dateOuverture = LocalDateTime.now();
        this.nbCollectes = 0;
    }
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "CYC-" + System.currentTimeMillis();
        }
    }
}