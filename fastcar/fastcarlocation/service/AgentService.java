package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier de l'entité Agent.
 */
@Service // Marque la classe comme un bean de service Spring
@RequiredArgsConstructor // Génère un constructeur avec les champs finaux (pour injection)
@Transactional // Assure que les méthodes sont exécutées dans une transaction
public class AgentService {

    private final AgentRepository agentRepository;

    /**
     * Enregistre un nouvel agent.
     */
    public Agent save(Agent agent) {
        // Logique métier : ici, on pourrait ajouter une validation complexe
        return agentRepository.save(agent);
    }

    /**
     * Récupère tous les agents.
     */
    @Transactional(readOnly = true) // Lecture seule = optimisation
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    /**
     * Récupère un agent par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Agent> findById(Long id) {
        return agentRepository.findById(id);
    }

    /**
     * Supprime un agent par son ID.
     */
    public void deleteById(Long id) {
        agentRepository.deleteById(id);
    }
}