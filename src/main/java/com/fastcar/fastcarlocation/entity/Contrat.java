package com.fastcar.fastcarlocation.entity;

import com.fastcar.fastcarlocation.enums.ModePaiement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
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
@Table(name = "contrat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id // Clé primaire : Numéro du contrat
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_contract")
    private Long numContract; // Numéro du contrat (ID auto-incrémenté)

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut")
    private LocalDate dateDebut; // Date de début de la location

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_fin")
    private LocalDate dateFin; // Date de fin de la location

    @NotNull(message = "Le montant total est obligatoire")
    @Column(name = "montant_t")
    private BigDecimal montantT; // Montant total à payer

    @Enumerated(EnumType.STRING) // Stocke le mode de paiement comme une chaîne
    @NotNull(message = "Le mode de paiement est obligatoire")
    @Column(name = "mode_paiement")
    private ModePaiement modePaiement; // Mode de paiement

    // --- Relations (Clés Étrangères) ---

    // 1. Relation ManyToOne avec Agent : Un contrat est géré par UN seul agent
    @ManyToOne
    @JoinColumn(name = "num_agent", nullable = false) // Colonne de la clé étrangère dans la table Contrat
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Agent agent;

    // 2. Relation ManyToOne avec Voiture : Un contrat concerne UNE seule voiture
    @ManyToOne
    @JoinColumn(name = "matricule", nullable = false) // Colonne de la clé étrangère dans la table Contrat
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Voiture voiture;

    // 3. Relation ManyToOne avec Client : Un contrat est fait par UN seul client
    @ManyToOne
    @JoinColumn(name = "cin_cli", nullable = false) // Colonne de la clé étrangère dans la table Contrat
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Client client;

}