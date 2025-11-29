package com.fastcar.fastcarlocation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id // Marque CIN comme la clé primaire
    @NotBlank(message = "Le CIN est obligatoire")
    private String cin; [cite_start]// CIN du client (identifiant unique) [cite: 32]

    @NotBlank(message = "Le nom du client est obligatoire")
    private String nomCli; [cite_start]// Nom du client [cite: 33]

    @NotBlank(message = "Le prénom du client est obligatoire")
    private String prenomCli; [cite_start]// Prénom du client [cite: 33]

    private String adresse; [cite_start]// Adresse complète [cite: 34]
    private String telephone; [cite_start]// Téléphone [cite: 35]

    @Email(message = "Le format de l'email n'est pas valide")
    private String email; [cite_start]// Email [cite: 35]

    [cite_start]// Relation : Un Client peut avoir plusieurs Contrats (locations) [cite: 41]
    // MappedBy indique le champ de mapping dans la classe Contrat (c'est l'entité 'Client' qui possède le CIN)
    // CascadeType.ALL assure que les opérations (delete) se propagent aux contrats liés.
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Contrat> contrats;
}
