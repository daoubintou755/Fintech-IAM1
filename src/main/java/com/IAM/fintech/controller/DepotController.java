package com.IAM.fintech.controller;

import com.IAM.fintech.Service.FintechService;
import com.IAM.fintech.repository.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/agent")
public class DepotController {

    @Autowired private FintechService fintechService;

    // AFFICHER LE TABLEAU DE BORD : Supprimé d'ici car maintenant dans AgentController

    // GÉRER LE DÉPÔT : On le garde ici
    //@PostMapping("/depot/add")
   // public String validerDepot(@RequestParam Long cycleId,
                            //   @RequestParam Integer nbMises,
                              // RedirectAttributes redirectAttributes) {
       // try {
           // fintechService.createDepot(cycleId, nbMises);
            //redirectAttributes.addFlashAttribute("message", "Dépôt effectué avec succès !");
        //} catch (Exception e) {
          //  redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
       // }
       // return "redirect:/agent/dashboard";
   // }

}