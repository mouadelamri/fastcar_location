package com.fastcar.controller;

import com.fastcar.model.Agent;
import com.fastcar.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "*")
public class AgentController {
    
    @Autowired
    private AgentRepository agentRepository;
    
    // GET tous les agents
    @GetMapping
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }
    
    // GET agent par numéro
    @GetMapping("/{numAgent}")
    public ResponseEntity<Agent> getAgentByNumero(@PathVariable String numAgent) {
        Optional<Agent> agent = agentRepository.findById(numAgent);
        return agent.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    // GET prochain numéro d'agent
    @GetMapping("/next-numero")
    public ResponseEntity<Map<String, String>> getNextAgentNumber() {
        Optional<String> maxNumAgent = agentRepository.findMaxNumAgent();
        
        String nextNumero;
        if (maxNumAgent.isPresent()) {
            String lastNum = maxNumAgent.get();
            // Extraire le nombre du numéro (ex: AG-100 -> 100)
            Pattern pattern = Pattern.compile("AG-(\\d+)");
            Matcher matcher = pattern.matcher(lastNum);
            
            if (matcher.find()) {
                int lastNumber = Integer.parseInt(matcher.group(1));
                nextNumero = "AG-" + (lastNumber + 1);
            } else {
                nextNumero = "AG-101";
            }
        } else {
            nextNumero = "AG-100";
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("nextNumero", nextNumero);
        return ResponseEntity.ok(response);
    }
    
    // POST ajouter un agent
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        // Vérifier si le numéro existe déjà
        if (agentRepository.existsById(agent.getNumAgent())) {
            return ResponseEntity.badRequest().body(null);
        }
        
        Agent savedAgent = agentRepository.save(agent);
        return ResponseEntity.ok(savedAgent);
    }
    
    // PUT modifier un agent
    @PutMapping("/{numAgent}")
    public ResponseEntity<Agent> updateAgent(@PathVariable String numAgent, 
                                            @RequestBody Agent agentDetails) {
        Optional<Agent> optionalAgent = agentRepository.findById(numAgent);
        
        if (optionalAgent.isPresent()) {
            Agent agent = optionalAgent.get();
            agent.setNomAgent(agentDetails.getNomAgent());
            agent.setPrenomAgent(agentDetails.getPrenomAgent());
            
            Agent updatedAgent = agentRepository.save(agent);
            return ResponseEntity.ok(updatedAgent);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // DELETE supprimer un agent
    @DeleteMapping("/{numAgent}")
    public ResponseEntity<Map<String, Boolean>> deleteAgent(@PathVariable String numAgent) {
        Optional<Agent> agent = agentRepository.findById(numAgent);
        
        if (agent.isPresent()) {
            agentRepository.delete(agent.get());
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // GET rechercher des agents
    @GetMapping("/search")
    public List<Agent> searchAgents(@RequestParam String keyword) {
        return agentRepository.search(keyword);
    }
    
    // GET agents par nom
    @GetMapping("/search/nom")
    public List<Agent> searchAgentsByNom(@RequestParam String nom) {
        return agentRepository.findByNomAgentContainingIgnoreCase(nom);
    }
    
    // GET agents par prénom
    @GetMapping("/search/prenom")
    public List<Agent> searchAgentsByPrenom(@RequestParam String prenom) {
        return agentRepository.findByPrenomAgentContainingIgnoreCase(prenom);
    }
    
    // GET compter les agents
    @GetMapping("/count")
    public Map<String, Long> countAgents() {
        Map<String, Long> response = new HashMap<>();
        response.put("total", agentRepository.count());
        return response;
    }
} // <-- AJOUTER CETTE ACCOLADE FERMANTE SI MANQUANTE