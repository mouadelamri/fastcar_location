package com.fastcar.fastcarlocation.entity;

import com.fastcar.fastcarlocation.enums.EtatVehicule;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entité Voiture
 * Correspond à la table 'voiture' dans la base de données.
 * Le Matricule est l'identifiant unique.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voiture {

    @Id // Marque Matricule comme la clé primaire
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule; [cite_start]// Matricule du véhicule (identifiant unique) [cite: 25]

    @NotBlank(message = "La marque est obligatoire")
    private String marque; [cite_start]// Marque du véhicule [cite: 26]

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele; [cite_start]// Modèle du véhicule [cite: 26]

    @NotNull(message = "Le prix journalier est obligatoire")
    private BigDecimal prixLoc; [cite_start]// Prix journalier de location [cite: 27] (Utilisation de BigDecimal pour la précision monétaire)

    @NotNull(message = "Le kilométrage est obligatoire")
    private Long kilometrage; [cite_start]// Kilométrage actuel [cite: 29]

    // Utilisation de l'énumération pour garantir l'état correct
    @Enumerated(EnumType.STRING) // Stocke la valeur comme une chaîne de caractères ('DISPONIBLE')
    @NotNull(message = "L'état du véhicule est obligatoire")
    private EtatVehicule etatVeh; [cite_start]// État du véhicule (Disponible, Louée, En maintenance) [cite: 28]

    // Relation : Une Voiture peut être dans plusieurs Contrats au fil du temps
    @OneToMany(mappedBy = "voiture", cascade = CascadeType.ALL)
    private List<Contrat> contrats;
}