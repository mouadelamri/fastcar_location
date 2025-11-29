package com.fastcar.fastcarlocation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entité Agent
 * Correspond à la table 'agent' dans la base de données.
 */
@Entity
@Data // Fournit les Getters, Setters, toString, equals et hashCode (via Lombok)
@NoArgsConstructor // Génère un constructeur sans arguments (requis par JPA)
@AllArgsConstructor // Génère un constructeur avec tous les arguments
public class Agent {

    @Id // Marque NumAgent comme la clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long numAgent;

    // Colonnes pour le nom et le prénom
    private String nomAgent;
    private String prenomAgent;

    [cite_start]// Remarque: L'étude de cas mentionne un 'Numéro d'agent (identifiant unique)' [cite: 38]
    // mais le type Long Auto-incrémenté est la méthode standard de Spring/JPA.
    [cite_start]// Si l'ID doit être une chaîne de caractères spécifique (ex: AG-205 [cite: 67]), 
    // l'annotation @GeneratedValue devra être retirée et la gestion de l'ID déplacée dans la couche Service.
}