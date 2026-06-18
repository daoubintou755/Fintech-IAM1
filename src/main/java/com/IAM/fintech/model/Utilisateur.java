package com.IAM.fintech.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    // Le rôle sera soit "ROLE_ADMIN", soit "ROLE_AGENT"
    private String role;
    // Nouveaux champs demandés
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String dateNaissance;
    private String adresse;
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();
}