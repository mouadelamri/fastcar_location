package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.entity.Contrat;
import com.fastcar.fastcarlocation.service.ContratService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des Contrats.
 * Les endpoints sont préfixés par /api/contracts.
 */
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContratController {

    private final ContratService contratService;

    // POST /api/contracts
    // Crée un nouveau contrat (Déclenche la logique de calcul de prix et mise à jour de l'état du véhicule)
    @PostMapping
    public ResponseEntity<Contrat> createContract(@Valid @RequestBody Contrat contrat) {
        try {
            Contrat savedContrat = contratService.createContract(contrat);
            return new ResponseEntity<>(savedContrat, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Gérer les erreurs métier (ex: voiture non disponible, client/agent non trouvé)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // GET /api/contracts
    // Récupère la liste de tous les contrats
    @GetMapping
    public ResponseEntity<List<Contrat>> getAllContracts() {
        List<Contrat> contrats = contratService.findAll();
        return ResponseEntity.ok(contrats);
    }

    // GET /api/contracts/{id}
    // Récupère un contrat par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getContractById(@PathVariable Long id) {
        return contratService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/contracts/{id}/end
    // Marque la fin d'un contrat de location et met la voiture en "Disponible"
    @PutMapping("/{id}/end")
    public ResponseEntity<Void> endContract(@PathVariable Long id) {
        try {
            contratService.endContract(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Gérer le cas où le contrat n'est pas trouvé
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE /api/contracts/{id}
    // Supprime un contrat (Opération moins courante mais nécessaire pour l'API CRUD complète)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        if (contratService.findById(id).isPresent()) {
            // NOTE: Une vraie application exigerait d'abord de vérifier l'état du véhicule
            // et de le rendre disponible si la suppression est faite avant la fin de contrat.
            contratService.endContract(id); // Mieux vaut mettre la voiture disponible par précaution
            contratService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}