package com.fastcar.fastcarlocation.entity;

import com.fastcar.fastcarlocation.enums.ModePaiement;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entité Contrat
 * Représente un contrat de location entre un client, une voiture et un agent.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id // Clé primaire : Numéro du contrat
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numContract; // Numéro du contrat (ID auto-incrémenté)

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut; [cite_start]// Date de début de la location [cite: 21]

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin; [cite_start]// Date de fin de la location [cite: 21]

    @NotNull(message = "Le montant total est obligatoire")
    private BigDecimal montantT; [cite_start]// Montant total à payer [cite: 22]

    @Enumerated(EnumType.STRING) // Stocke le mode de paiement comme une chaîne
    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement; [cite_start]// Mode de paiement [cite: 23]

    // --- Relations (Clés Étrangères) ---

    [cite_start]// 1. Relation ManyToOne avec Agent : Un contrat est géré par UN seul agent [cite: 44]
    @ManyToOne
    @NotNull
    @JoinColumn(name = "num_agent") // Colonne de la clé étrangère dans la table Contrat
    private Agent agent;

    [cite_start]// 2. Relation ManyToOne avec Voiture : Un contrat concerne UNE seule voiture [cite: 44]
    @ManyToOne
    @NotNull
    @JoinColumn(name = "matricule") // Colonne de la clé étrangère dans la table Contrat
    private Voiture voiture;

    [cite_start]// 3. Relation ManyToOne avec Client : Un contrat est fait par UN seul client [cite: 44]
    @ManyToOne
    @NotNull
    @JoinColumn(name = "cin_cli") // Colonne de la clé étrangère dans la table Contrat
    private Client client;

}