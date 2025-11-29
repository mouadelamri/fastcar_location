package com.fastcar.fastcarlocation.repository;

import com.fastcar.fastcarlocation.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Agent.
 * Fournit automatiquement les méthodes CRUD (FindAll, Save, Delete, etc.).
 * Long est le type de la clé primaire (numAgent).
 */
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    // Aucune méthode d'implémentation nécessaire ici.
    // Vous pouvez ajouter des méthodes de recherche personnalisées si besoin.
}