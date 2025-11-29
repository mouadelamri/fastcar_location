package com.fastcar.fastcarlocation.entity;

import com.fastcar.fastcarlocation.enums.EtatVehicule;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
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
@Table(name = "voiture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voiture {

    @Id // Marque Matricule comme la clé primaire
    @NotBlank(message = "Le matricule est obligatoire")
    @Column(name = "matricule")
    private String matricule; // Matricule du véhicule (identifiant unique)

    @NotBlank(message = "La marque est obligatoire")
    @Column(name = "marque")
    private String marque; // Marque du véhicule

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(name = "modele")
    private String modele; // Modèle du véhicule

    @NotNull(message = "Le prix journalier est obligatoire")
    @Column(name = "prix_loc")
    private BigDecimal prixLoc; // Prix journalier de location (Utilisation de BigDecimal pour la précision monétaire)

    @NotNull(message = "Le kilométrage est obligatoire")
    @Column(name = "kilometrage")
    private Long kilometrage; // Kilométrage actuel

    // Utilisation de l'énumération pour garantir l'état correct
    @Enumerated(EnumType.STRING) // Stocke la valeur comme une chaîne de caractères ('DISPONIBLE')
    @NotNull(message = "L'état du véhicule est obligatoire")
    @Column(name = "etat_veh")
    private EtatVehicule etatVeh; // État du véhicule (Disponible, Louée, En maintenance)

    // Relation : Une Voiture peut être dans plusieurs Contrats au fil du temps
    @OneToMany(mappedBy = "voiture", cascade = CascadeType.ALL)
    private List<Contrat> contrats;

    // Getters and setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public BigDecimal getPrixLoc() {
        return prixLoc;
    }

    public void setPrixLoc(BigDecimal prixLoc) {
        this.prixLoc = prixLoc;
    }

    public Long getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(Long kilometrage) {
        this.kilometrage = kilometrage;
    }

    public EtatVehicule getEtatVeh() {
        return etatVeh;
    }

    public void setEtatVeh(EtatVehicule etatVeh) {
        this.etatVeh = etatVeh;
    }
}