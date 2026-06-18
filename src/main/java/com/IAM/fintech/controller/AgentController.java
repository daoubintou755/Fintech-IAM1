package com.IAM.fintech.controller;

import com.IAM.fintech.dto.ClientDashboardDTO;
import com.IAM.fintech.model.*;
import org.springframework.transaction.annotation.Transactional;
import com.IAM.fintech.repository.*;
import com.IAM.fintech.Service.FintechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agent")
public class AgentController {

    @Autowired private CycleRepository cycleRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private FintechService fintechService;
    @Autowired private MouvementRepository mouvRepo;
    @Autowired private UtilisateurRepository utilisateurRepo;

    @GetMapping("/dashboard")
    public String agentDashboard(Model model, Principal principal) {
        Utilisateur agent = utilisateurRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));
        List<Client> clients = clientRepo.findByAgent(agent);
        List<ClientDashboardDTO> clientDTOs = clients.stream().map(c -> {
            Double solde = fintechService.getMontantRetirable(c.getId());
            return new ClientDashboardDTO(c.getId(), c.getNom(), c.getPrenom(), c.getTelephone(), solde);
        }).collect(Collectors.toList());

        model.addAttribute("transactions", mouvRepo.findByAgent(agent));
        model.addAttribute("clients", clientDTOs);
        model.addAttribute("allClients", clients);
        model.addAttribute("cycles", cycleRepo.findByStatus("EN_COURS"));
        return "agent_dashboard";
    }

    @PostMapping("/cycle/add")
    public String addCycle(@RequestParam Long clientId,
                           @RequestParam String nom,
                           @RequestParam Double montantBase,
                           Principal principal,
                           RedirectAttributes ra) {
        try {
            Utilisateur agent = utilisateurRepo.findByUsername(principal.getName()).orElseThrow();
            Client client = clientRepo.findByIdAndAgent(clientId, agent)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            Cycle cycle = new Cycle();
            cycle.setNom(nom);
            cycle.setMise(montantBase);
            cycle.setClient(client);
            cycle.setStatus("EN_COURS");
            cycleRepo.save(cycle);
            ra.addFlashAttribute("message", "Cycle créé avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/agent/dashboard";
    }

    @PostMapping("/client/add")
    @Transactional
    public String addClient(@ModelAttribute Client client, Principal principal) {
        Utilisateur agent = utilisateurRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Agent non connecté"));
        client.setAgent(agent);
        clientRepo.saveAndFlush(client);
        return "redirect:/agent/dashboard";
    }

    @PostMapping("/depot/add")
    public String validerDepot(@RequestParam Long cycleId,
                               @RequestParam Integer nbMises,
                               Principal principal,
                               RedirectAttributes ra) {
        try {
            Utilisateur agent = utilisateurRepo.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Agent non connecté"));
            fintechService.createDepot(cycleId, nbMises, agent);
            ra.addFlashAttribute("message", "Dépôt validé avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/agent/dashboard";
    }

    @PostMapping("/retrait/add")
    public String validerRetrait(@RequestParam Long clientId,
                                 @RequestParam Double montant,
                                 Principal principal,
                                 RedirectAttributes ra) {
        try {
            Utilisateur agent = utilisateurRepo.findByUsername(principal.getName()).orElseThrow();
            fintechService.createRetrait(clientId, montant, agent);
            ra.addFlashAttribute("message", "Retrait effectué avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/agent/dashboard";
    }

    @GetMapping("/client/details/{id}")
    public String getClientDetails(@PathVariable Long id, Model model, Principal principal, RedirectAttributes ra) {
        Utilisateur agent = utilisateurRepo.findByUsername(principal.getName()).orElseThrow();
        return clientRepo.findByIdAndAgent(id, agent)
                .map(client -> {
                    model.addAttribute("client", client);
                    model.addAttribute("transactions", fintechService.getHistoriqueTransactions(id));
                    return "client_details";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Client introuvable.");
                    return "redirect:/agent/dashboard";
                });
    }
} // <--- Cette accolade ferme la classe AgentController