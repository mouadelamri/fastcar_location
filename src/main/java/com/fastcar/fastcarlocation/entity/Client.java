package com.fastcar.fastcarlocation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité Client
 * Correspond à la table 'client' dans la base de données.
 * Le CIN est l'identifiant unique.
 */
@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id // Marque CIN comme la clé primaire
    @NotBlank(message = "Le CIN est obligatoire")
    @Column(name = "cin_cli")
    private String cin; // CIN du client (identifiant unique)

    @NotBlank(message = "Le nom du client est obligatoire")
    @Column(name = "nom_cli")
    private String nomCli; // Nom du client

    @NotBlank(message = "Le prénom du client est obligatoire")
    @Column(name = "prenom_cli")
    private String prenomCli; // Prénom du client

    @Column(name = "adresse")
    private String adresse; // Adresse complète

    @Column(name = "telephone")
    private String telephone; // Téléphone

    @Email(message = "Le format de l'email n'est pas valide")
    @Column(name = "email")
    private String email; // Email

    // Relation : Un Client peut avoir plusieurs Contrats (locations)
    // MappedBy indique le champ de mapping dans la classe Contrat (c'est l'entité 'Client' qui possède le CIN)
    // CascadeType.ALL assure que les opérations (delete) se propagent aux contrats liés.
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Contrat> contrats;

    // Getters and setters
    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNomCli() {
        return nomCli;
    }

    public void setNomCli(String nomCli) {
        this.nomCli = nomCli;
    }

    public String getPrenomCli() {
        return prenomCli;
    }

    public void setPrenomCli(String prenomCli) {
        this.prenomCli = prenomCli;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

