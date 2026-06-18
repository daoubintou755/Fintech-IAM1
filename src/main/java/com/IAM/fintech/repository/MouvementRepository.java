package com.IAM.fintech.repository;

import com.IAM.fintech.model.Mouvement;
import com.IAM.fintech.model.Utilisateur; // Import nécessaire
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Long> {

    // 1. HISTORIQUE GLOBAL (Admin Dashboard)
    List<Mouvement> findAllByOrderByDateDesc();

    // 2. FILTRAGE PAR AGENT (Correction : on utilise l'objet Utilisateur)
    // Spring Data JPA va utiliser le champ 'agent' de la classe Mouvement
    List<Mouvement> findByAgent(Utilisateur agent);

    // 3. Calcul du solde
    List<Mouvement> findAllByClientId(Long clientId);

    // 4. Historique spécifique client
    List<Mouvement> findAllByClientIdOrderByDateDesc(Long clientId);

    // Exemple si vous voulez calculer le total par type plus tard
    @Query("SELECT SUM(m.amount) FROM Mouvement m WHERE m.client.id = :clientId AND m.type = :type")
    Double sumAmountByClientIdAndType(Long clientId, String type);
}