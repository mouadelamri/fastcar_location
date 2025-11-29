package com.fastcar.fastcarlocation.dto;

import com.fastcar.fastcarlocation.enums.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour la réponse d'un contrat.
 * Utilisé pour retourner les données du contrat sans exposer directement les entités JPA.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratResponseDTO {
    
    private Long numContract;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montantT;
    private ModePaiement modePaiement;
    
    // Informations simplifiées des entités liées
    private Long numAgent;
    private String nomAgent;
    private String prenomAgent;
    
    private String matricule;
    private String marque;
    private String modele;
    
    private String cinCli;
    private String nomCli;
    private String prenomCli;
}

