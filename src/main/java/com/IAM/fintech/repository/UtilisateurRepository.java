package com.IAM.fintech.repository;

import com.IAM.fintech.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Import nécessaire
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Utilisé par Spring Security pour la connexion
    Optional<Utilisateur> findByUsername(String username);

    // Ajoutez cette ligne pour votre AdminController
    List<Utilisateur> findByRole(String role);
}