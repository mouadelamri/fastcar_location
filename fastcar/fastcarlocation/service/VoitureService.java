package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Voiture;
import com.fastcar.fastcarlocation.enums.EtatVehicule;
import com.fastcar.fastcarlocation.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier de l'entité Voiture.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VoitureService {

    private final VoitureRepository voitureRepository;

    public Voiture save(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    @Transactional(readOnly = true)
    public List<Voiture> findAll() {
        return voitureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Voiture> findById(String matricule) {
        return voitureRepository.findById(matricule);
    }

    public void deleteById(String matricule) {
        voitureRepository.deleteById(matricule);
    }

    // Logique métier spécifique : Trouver toutes les voitures disponibles
    @Transactional(readOnly = true)
    public List<Voiture> findAvailableCars() {
        // En Spring Data JPA, on peut ajouter une méthode dans le Repository
        // qui sera implicitement implémentée, mais ici on le fait par filtrage simple.
        return voitureRepository.findAll().stream()
                .filter(v -> v.getEtatVeh() == EtatVehicule.DISPONIBLE)
                .toList();
    }
}