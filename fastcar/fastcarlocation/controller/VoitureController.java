package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.entity.Voiture;
import com.fastcar.fastcarlocation.service.VoitureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des Voitures.
 * Les endpoints sont préfixés par /api/voitures.
 */
@RestController
@RequestMapping("/api/voitures")
@RequiredArgsConstructor
public class VoitureController {

    private final VoitureService voitureService;

    // POST /api/voitures
    // Crée une nouvelle voiture
    @PostMapping
    public ResponseEntity<Voiture> createVoiture(@Valid @RequestBody Voiture voiture) {
        Voiture savedVoiture = voitureService.save(voiture);
        return new ResponseEntity<>(savedVoiture, HttpStatus.CREATED);
    }

    // GET /api/voitures
    // Récupère la liste de toutes les voitures
    @GetMapping
    public ResponseEntity<List<Voiture>> getAllVoitures() {
        List<Voiture> voitures = voitureService.findAll();
        return ResponseEntity.ok(voitures);
    }

    // GET /api/voitures/available
    // Endpoint spécifique pour trouver les voitures DISPONIBLES
    @GetMapping("/available")
    public ResponseEntity<List<Voiture>> getAvailableVoitures() {
        List<Voiture> availableCars = voitureService.findAvailableCars();
        return ResponseEntity.ok(availableCars);
    }

    // GET /api/voitures/{matricule}
    // Récupère une voiture par son Matricule
    @GetMapping("/{matricule}")
    public ResponseEntity<Voiture> getVoitureByMatricule(@PathVariable String matricule) {
        return voitureService.findById(matricule)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/voitures/{matricule}
    // Met à jour une voiture existante
    @PutMapping("/{matricule}")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable String matricule, @Valid @RequestBody Voiture voitureDetails) {
        return voitureService.findById(matricule)
                .map(existingVoiture -> {
                    // Mettre à jour les champs
                    existingVoiture.setMarque(voitureDetails.getMarque());
                    existingVoiture.setModele(voitureDetails.getModele());
                    existingVoiture.setPrixLoc(voitureDetails.getPrixLoc());
                    existingVoiture.setKilometrage(voitureDetails.getKilometrage());
                    existingVoiture.setEtatVeh(voitureDetails.getEtatVeh());
                    
                    Voiture updatedVoiture = voitureService.save(existingVoiture);
                    return ResponseEntity.ok(updatedVoiture);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/voitures/{matricule}
    // Supprime une voiture
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable String matricule) {
        if (voitureService.findById(matricule).isPresent()) {
            voitureService.deleteById(matricule);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
