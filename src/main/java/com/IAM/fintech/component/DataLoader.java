package com.IAM.fintech.component;

import com.IAM.fintech.model.Utilisateur;
import com.IAM.fintech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UtilisateurRepository utilisateurRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private CycleRepository cycleRepo;
    @Autowired private MouvementRepository mouvRepo;
    @Autowired private DepotRepository depotRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Nettoyage de la base
        mouvRepo.deleteAll();
        depotRepo.deleteAll();
        cycleRepo.deleteAll();
        clientRepo.deleteAll();
        utilisateurRepo.deleteAll();

        // Création Admin
        if (utilisateurRepo.findByUsername("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            utilisateurRepo.save(admin);
        }

        // Création Agent
        if (utilisateurRepo.findByUsername("agent1").isEmpty()) {
            Utilisateur agent = new Utilisateur();
            agent.setUsername("agent1");
            agent.setPassword(passwordEncoder.encode("agent123"));
            agent.setRole("ROLE_AGENT");
            utilisateurRepo.save(agent);
        }

        System.out.println(">> [DATABASE] Base de données réinitialisée !");
    } // <-- Ferme la méthode run
} // <-- Ferme la classe DataLoader