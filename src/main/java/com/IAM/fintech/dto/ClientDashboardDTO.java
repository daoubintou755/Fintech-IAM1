package com.IAM.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDashboardDTO {

    private Long id;
    private String nom;
    private String Prenom;
    private String telephone;
    private Double soldeActuel; // Le solde calculé via fintech

} // <--- Cette accolade était manquante