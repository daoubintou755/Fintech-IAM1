package com.IAM.fintech.service;

import com.IAM.fintech.model.Depot;
import com.IAM.fintech.model.Mouvement;
import com.IAM.fintech.repository.DepotRepository;
import com.IAM.fintech.repository.MouvementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private DepotRepository depotRepository;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Transactional
    public void enregistrerDepot(Depot depot) {
        // 1. Sauvegarde dans la table Depot
        depotRepository.save(depot);

        // 2. Trace dans la table Mouvement
        Mouvement mvt = new Mouvement();
        mvt.setCode(depot.getCode());
        mvt.setType("DEPOT");
        mvt.setAmount(depot.getMontant());
        mvt.setClient(depot.getClient());
        mvt.setDate(depot.getDate());
        mouvementRepository.save(mvt);
    }
}