package com.IAM.fintech.controller;

import com.IAM.fintech.model.Mouvement;
import com.IAM.fintech.model.Utilisateur;
import com.IAM.fintech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private ClientRepository clientRepository;
    @Autowired private CycleRepository cycleRepository;
    @Autowired private MouvementRepository mouvementRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // CORRECTION : Utilisez les noms de variables déclarés ci-dessus
        List<Utilisateur> agents = utilisateurRepository.findAll();
        List<Mouvement> transactions = mouvementRepository.findAll();

        model.addAttribute("agents", agents);
        model.addAttribute("transactions", transactions);

        return "admin_dashboard";
    }

    @PostMapping("/agent/add")
    public String addAgent(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String telephone,
            @RequestParam String email) {

        Utilisateur agent = new Utilisateur();
        agent.setUsername(username);
        // Utilisation correcte du passwordEncoder
        agent.setPassword(passwordEncoder.encode(password));
        agent.setNom(nom);
        agent.setPrenom(prenom);
        agent.setTelephone(telephone);
        agent.setEmail(email);
        agent.setRole("ROLE_AGENT");

        utilisateurRepository.save(agent);

        return "redirect:/admin/dashboard";
    }
}