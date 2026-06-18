package com.IAM.fintech.repository;

import com.IAM.fintech.model.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {

    // Ajout de cette méthode pour filtrer les cycles selon leur statut (ex: "EN_COURS")
    List<Cycle> findByStatus(String status);
}