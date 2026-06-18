package com.IAM.fintech.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String nom;
    private String prenom;
    private String genre;
    private LocalDate dateNaissance;
    private String email;
    private String telephone;
    private String adresse;

    // Correction : Une seule relation vers l'agent (de type Utilisateur)
    @ManyToOne
    @JoinColumn(name = "agent_id") // Le nom de la colonne dans la table client
    private Utilisateur agent;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Cycle> cycles = new ArrayList<>();
}