package com.fastcar.fastcarlocation.dto;

import com.fastcar.fastcarlocation.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour la création d'un contrat.
 * Utilisé pour recevoir les données du client sans exposer directement les entités JPA.
 */
@Data
public class ContratDTO {
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;
    
    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;
    
    @NotNull(message = "L'ID de l'agent est obligatoire")
    private Long numAgent;
    
    @NotNull(message = "Le matricule du véhicule est obligatoire")
    private String matricule;
    
    @NotNull(message = "Le CIN du client est obligatoire")
    private String cinCli;
    
    // Le montant total sera calculé automatiquement par le service
    // mais peut être fourni pour validation
    private BigDecimal montantT;
}

