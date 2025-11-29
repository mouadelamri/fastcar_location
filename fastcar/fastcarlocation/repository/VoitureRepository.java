package com.fastcar.fastcarlocation.repository;

import com.fastcar.fastcarlocation.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Voiture.
 * String est le type de la clé primaire (matricule).
 */
@Repository
public interface VoitureRepository extends JpaRepository<Voiture, String> {
}