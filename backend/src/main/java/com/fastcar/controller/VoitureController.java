package com.fastcar.controller;

import com.fastcar.model.Voiture;
import com.fastcar.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/voitures")
@CrossOrigin(origins = "*")
public class VoitureController {
    
    @Autowired
    private VoitureRepository voitureRepository;
    
    // GET toutes les voitures
    @GetMapping
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }
    
    // GET voiture par matricule
    @GetMapping("/{matricule}")
    public ResponseEntity<Voiture> getVoitureByMatricule(@PathVariable String matricule) {
        Optional<Voiture> voiture = voitureRepository.findById(matricule);
        return voiture.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET voitures disponibles
    @GetMapping("/disponibles")
    public List<Voiture> getVoituresDisponibles() {
        return voitureRepository.findVoituresDisponibles();
    }
    
    // GET voitures louées
    @GetMapping("/louees")
    public List<Voiture> getVoituresLouees() {
        return voitureRepository.findVoituresLouees();
    }
    
    // GET voitures par état
    @GetMapping("/etat/{etat}")
    public List<Voiture> getVoituresParEtat(@PathVariable String etat) {
        return voitureRepository.findByEtatVeh(etat);
    }
    
    // POST ajouter une voiture
    @PostMapping
    public Voiture createVoiture(@RequestBody Voiture voiture) {
        return voitureRepository.save(voiture);
    }
    
    // PUT modifier une voiture
    @PutMapping("/{matricule}")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable String matricule, 
                                                 @RequestBody Voiture voitureDetails) {
        Optional<Voiture> optionalVoiture = voitureRepository.findById(matricule);
        
        if (optionalVoiture.isPresent()) {
            Voiture voiture = optionalVoiture.get();
            
            voiture.setMarque(voitureDetails.getMarque());
            voiture.setModele(voitureDetails.getModele());
            voiture.setPrixLoc(voitureDetails.getPrixLoc());
            voiture.setEtatVeh(voitureDetails.getEtatVeh());
            voiture.setKilometrage(voitureDetails.getKilometrage());
            
            Voiture updatedVoiture = voitureRepository.save(voiture);
            return ResponseEntity.ok(updatedVoiture);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // PATCH modifier seulement le kilométrage
    @PatchMapping("/{matricule}/kilometrage")
    public ResponseEntity<Voiture> updateKilometrage(@PathVariable String matricule, 
                                                     @RequestBody Map<String, Integer> updates) {
        Optional<Voiture> optionalVoiture = voitureRepository.findById(matricule);
        
        if (optionalVoiture.isPresent()) {
            Voiture voiture = optionalVoiture.get();
            voiture.setKilometrage(updates.get("kilometrage"));
            
            Voiture updatedVoiture = voitureRepository.save(voiture);
            return ResponseEntity.ok(updatedVoiture);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // PATCH modifier seulement l'état
    @PatchMapping("/{matricule}/etat")
    public ResponseEntity<Voiture> updateEtat(@PathVariable String matricule,
                                              @RequestBody Map<String, String> updates) {
        Optional<Voiture> optionalVoiture = voitureRepository.findById(matricule);
        
        if (optionalVoiture.isPresent()) {
            Voiture voiture = optionalVoiture.get();
            voiture.setEtatVeh(updates.get("etat"));
            
            Voiture updatedVoiture = voitureRepository.save(voiture);
            return ResponseEntity.ok(updatedVoiture);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // DELETE supprimer une voiture
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Map<String, Boolean>> deleteVoiture(@PathVariable String matricule) {
        Optional<Voiture> voiture = voitureRepository.findById(matricule);
        
        if (voiture.isPresent()) {
            voitureRepository.delete(voiture.get());
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // GET rechercher des voitures
    @GetMapping("/search")
    public List<Voiture> searchVoitures(@RequestParam String keyword) {
        return voitureRepository.search(keyword);
    }
    
    // GET statistiques des voitures
    @GetMapping("/statistiques")
    public Map<String, Object> getStatistiquesVoitures() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", voitureRepository.count());
        stats.put("disponibles", voitureRepository.countByEtatVeh("Disponible"));
        stats.put("louees", voitureRepository.countByEtatVeh("Louée"));
        stats.put("maintenance", voitureRepository.countByEtatVeh("En maintenance"));
        
        return stats;
    }
}