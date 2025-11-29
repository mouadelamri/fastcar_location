package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des Agents.
 * Les endpoints sont préfixés par /api/agents.
 */
@RestController // Marque la classe comme un contrôleur REST
@RequestMapping("/api/agents") // Chemin de base pour toutes les méthodes de ce contrôleur
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    // POST /api/agents
    // Crée un nouvel agent
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent savedAgent = agentService.ajouterAgent(agent);
        return new ResponseEntity<>(savedAgent, HttpStatus.CREATED);
    }

    // GET /api/agents
    // Récupère la liste de tous les agents
    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = agentService.findAll();
        return ResponseEntity.ok(agents);
    }

    // GET /api/agents/{id}
    // Récupère un agent par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        return agentService.findById(id)
                .map(ResponseEntity::ok) // Si trouvé (200 OK)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si non trouvé (404 NOT FOUND)
    }

    // PUT /api/agents/{id}
    // Met à jour un agent existant
    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody Agent agentDetails) {
        return agentService.findById(id)
                .map(existingAgent -> {
                    // Mettre à jour les champs (on suppose que l'ID n'est pas modifié)
                    existingAgent.setNomAgent(agentDetails.getNomAgent());
                    existingAgent.setPrenomAgent(agentDetails.getPrenomAgent());
                    
                    Agent updatedAgent = agentService.ajouterAgent(existingAgent);
                    return ResponseEntity.ok(updatedAgent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/agents/{id}
    // Supprime un agent
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        if (agentService.findById(id).isPresent()) {
            agentService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 NO CONTENT (Suppression réussie)
        } else {
            return ResponseEntity.notFound().build(); // 404 NOT FOUND
        }
    }
}
