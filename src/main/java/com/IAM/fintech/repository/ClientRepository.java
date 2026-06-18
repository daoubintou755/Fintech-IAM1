package com.IAM.fintech.repository;

import com.IAM.fintech.model.Client;
import com.IAM.fintech.model.Utilisateur; // Import indispensable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // 1. Trouver tous les clients qui appartiennent à un agent spécifique
    List<Client> findByAgent(Utilisateur agent);

    // 2. Trouver un client précis par son ID ET son agent
    // (Crucial pour la sécurité : empêche un agent d'accéder au client d'un autre)
    Optional<Client> findByIdAndAgent(Long id, Utilisateur agent);
}