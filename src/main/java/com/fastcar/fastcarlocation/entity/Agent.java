package com.fastcar.fastcarlocation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité Agent
 * Correspond à la table 'agent' dans la base de données.
 */
@Entity
@Table(name = "agent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_agent")
    private Long numAgent;

    @NotBlank(message = "Le nom de l'agent est obligatoire")
    @Column(name = "nom_agent")
    private String nomAgent;

    @NotBlank(message = "Le prénom de l'agent est obligatoire")
    @Column(name = "prenom_agent")
    private String prenomAgent;

    // Relation : Un Agent peut gérer plusieurs Contrats
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Contrat> contrats;
}