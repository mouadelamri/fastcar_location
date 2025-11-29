package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.dto.ContratDTO;
import com.fastcar.fastcarlocation.dto.ContratResponseDTO;
import com.fastcar.fastcarlocation.mapper.ContratMapper;
import com.fastcar.fastcarlocation.service.ContratService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour la gestion des Contrats.
 * Les endpoints sont préfixés par /api/contrats.
 */
@RestController
@RequestMapping({"/api/contracts","/api/contrats"})
@RequiredArgsConstructor
public class ContratController {

    private final ContratService contratService;
    private final ContratMapper contratMapper;

    // POST /api/contracts
    // Crée un nouveau contrat (Déclenche la logique de calcul de prix et mise à jour de l'état du véhicule)
    // Les exceptions sont gérées par GlobalExceptionHandler
    @PostMapping
    public ResponseEntity<ContratResponseDTO> createContract(@Valid @RequestBody ContratDTO contratDTO) {
        var contrat = contratMapper.toEntity(contratDTO);
        var savedContrat = contratService.createContract(contrat);
        return new ResponseEntity<>(contratMapper.toResponseDTO(savedContrat), HttpStatus.CREATED);
    }

    // GET /api/contracts
    // Récupère la liste de tous les contrats
    @GetMapping
    public ResponseEntity<List<ContratResponseDTO>> getAllContracts() {
        List<ContratResponseDTO> result = contratService.findAll().stream()
            .map(contratMapper::toResponseDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // GET /api/contracts/{id}
    // Récupère un contrat par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ContratResponseDTO> getContractById(@PathVariable Long id) {
        return contratService.findById(id)
                .map(c -> ResponseEntity.ok(contratMapper.toResponseDTO(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/contracts/{id}/end
    // Marque la fin d'un contrat de location et met la voiture en "Disponible"
    // Les exceptions sont gérées par GlobalExceptionHandler
    @PutMapping("/{id}/end")
    public ResponseEntity<Void> endContract(@PathVariable Long id) {
        contratService.endContract(id);
        return ResponseEntity.noContent().build();
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