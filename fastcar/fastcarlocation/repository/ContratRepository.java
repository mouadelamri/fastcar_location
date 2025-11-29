package com.fastcar.fastcarlocation.repository;

import com.fastcar.fastcarlocation.entity.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Contrat.
 * Long est le type de la clé primaire (numContract).
 */
@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
}