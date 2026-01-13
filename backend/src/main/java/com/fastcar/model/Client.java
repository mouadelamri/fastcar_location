package com.fastcar.model;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "cin_cli")
    private String cinCli;
    
    @Column(name = "nom_cli")
    private String nomCli;
    
    @Column(name = "prenom_cli")
    private String prenomCli;
    
    private String adresse;
    private String telephone;
    private String email;
    
    // Getters et setters
    public String getCinCli() { return cinCli; }
    public void setCinCli(String cinCli) { this.cinCli = cinCli; }
    
    public String getNomCli() { return nomCli; }
    public void setNomCli(String nomCli) { this.nomCli = nomCli; }
    
    public String getPrenomCli() { return prenomCli; }
    public void setPrenomCli(String prenomCli) { this.prenomCli = prenomCli; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}