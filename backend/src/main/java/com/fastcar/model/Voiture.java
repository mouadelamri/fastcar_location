package com.fastcar.model;

import javax.persistence.*;

@Entity
@Table(name = "voiture")
public class Voiture {
    @Id
    private String matricule;
    
    private String marque;
    private String modele;
    
    @Column(name = "prix_loc")
    private double prixLoc;
    
    @Column(name = "etat_veh")
    private String etatVeh; // Garder comme String pour simplifier
    
    private Integer kilometrage;
    
    // Getters et setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    
    public double getPrixLoc() { return prixLoc; }
    public void setPrixLoc(double prixLoc) { this.prixLoc = prixLoc; }
    
    public String getEtatVeh() { return etatVeh; }
    public void setEtatVeh(String etatVeh) { this.etatVeh = etatVeh; }
    
    public Integer getKilometrage() { return kilometrage; }
    public void setKilometrage(Integer kilometrage) { this.kilometrage = kilometrage; }
}