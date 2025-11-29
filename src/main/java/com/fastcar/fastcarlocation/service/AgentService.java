package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.exception.EntityNotFoundException;
import com.fastcar.fastcarlocation.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier de l'entité Agent.
 */
@Service
@RequiredArgsConstructor
@Transactional
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
    @Transactional(readOnly = true)
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
     * Récupère un agent par son ID ou lance une exception si non trouvé.
     */
    @Transactional(readOnly = true)
    public Agent findByIdOrThrow(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agent non trouvé avec l'ID: " + id));
    }

    /**
     * Supprime un agent par son ID.
     */
    public void deleteById(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new EntityNotFoundException("Agent non trouvé avec l'ID: " + id);
        }
        agentRepository.deleteById(id);
    }

    public Agent ajouterAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    public List<Agent> consulterAgents() {
        return agentRepository.findAll();
    }

    public void supprimerAgent(Long numAgent) {
        deleteById(numAgent);
    }
}