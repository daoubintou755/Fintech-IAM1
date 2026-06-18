package com.IAM.fintech.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String telephone;
    private String password;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private String adresse;
    private String role;
}