package com.IAM.fintech.Service;

import com.IAM.fintech.model.*;
import com.IAM.fintech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FintechService {

    @Autowired private CycleRepository cycleRepo;
    @Autowired private DepotRepository depotRepo;
    @Autowired private MouvementRepository mouvRepo;
    @Autowired private ClientRepository clientRepo;

    // --- DÉPÔT ---
    public void createDepot(Long cycleId, Integer nbMises, Utilisateur agent) {
        Cycle cycle = cycleRepo.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle non trouvé"));

        if (!"EN_COURS".equals(cycle.getStatus())) {
            throw new IllegalStateException("Opération interdite : cycle déjà clôturé.");
        }

        if (cycle.getMise() == null || cycle.getMise() <= 0) {
            throw new IllegalArgumentException("La mise unitaire n'est pas définie.");
        }

        double montantTotal = nbMises * cycle.getMise();
        if (montantTotal % 100 != 0) {
            throw new IllegalArgumentException("Le montant total doit être un multiple de 100.");
        }

        if (cycle.getNbCollectes() + nbMises > 31) {
            throw new IllegalArgumentException("Dépôt refusé : dépassement de la limite.");
        }

        Depot depot = new Depot();
        depot.setCycle(cycle);
        depot.setNbMises(nbMises);
        depot.setMontant(montantTotal);
        depot.setDate(LocalDateTime.now());
        depot.setCode("DEP-" + UUID.randomUUID().toString().substring(0, 8));
        depotRepo.save(depot);

        saveMouvement(cycle, null, "MISE", depot.getMontant(), agent);

        cycle.setNbCollectes(cycle.getNbCollectes() + nbMises);
        cycleRepo.save(cycle);

        if (cycle.getNbCollectes() == 31) {
            closeCycle(cycle, agent);
        }
    }

    // --- CLÔTURE ---
    private void closeCycle(Cycle cycle, Utilisateur agent) {
        cycle.setStatus("CLOTURE");
        cycle.setDateFermeture(LocalDateTime.now());
        cycleRepo.save(cycle);

        double mise = cycle.getMise();

        saveMouvement(cycle, null, "RETENUE", mise, agent);
        saveMouvement(cycle, null, "COM_AGENT", mise / 2, agent);
        saveMouvement(cycle, null, "COM_INSTITUTION", mise / 2, agent);
        saveMouvement(cycle, null, "CREDIT_CLIENT", (mise * 31) - mise, agent);
    }

    // --- SAUVEGARDE MOUVEMENT ---
    private void saveMouvement(Cycle cycle, Client client, String type, Double montant, Utilisateur agent) {
        Mouvement m = new Mouvement();
        m.setCode("MOUV-" + UUID.randomUUID().toString().substring(0, 8));
        m.setType(type);
        m.setAmount(montant);
        m.setDate(LocalDateTime.now());
        // Si cycle est présent, on extrait le client, sinon on utilise le client passé en argument
        m.setClient(cycle != null ? cycle.getClient() : client);
        m.setCycle(cycle);
        m.setAgent(agent);
        mouvRepo.save(m);
    }

    // --- SOLDE ---
    public Double getMontantRetirable(Long clientId) {
        List<Mouvement> mouvements = mouvRepo.findAllByClientId(clientId);
        double totalCredit = mouvements.stream()
                .filter(m -> "CREDIT_CLIENT".equals(m.getType()) || "MISE".equals(m.getType()))
                .mapToDouble(Mouvement::getAmount).sum();
        double totalRetrait = mouvements.stream()
                .filter(m -> "RETRAIT".equals(m.getType()))
                .mapToDouble(Mouvement::getAmount).sum();
        return totalCredit - totalRetrait;
    }

    // --- RETRAIT ---
    public void createRetrait(Long clientId, Double montant, Utilisateur agent) {
        if (montant == null || montant <= 0) throw new IllegalArgumentException("Montant invalide.");

        Double disponible = getMontantRetirable(clientId);
        if (montant > disponible) throw new IllegalStateException("Solde insuffisant.");

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client inconnu"));

        // Ici on passe 'null' pour le cycle, et le 'client' récupéré
        saveMouvement(null, client, "RETRAIT", montant, agent);
    }

    // --- HISTORIQUE ---
    public List<Mouvement> getHistoriqueTransactions(Long clientId) {
        return mouvRepo.findAllByClientIdOrderByDateDesc(clientId);
    }
}