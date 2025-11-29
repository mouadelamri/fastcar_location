package com.fastcar.fastcarlocation.repository;

import com.fastcar.fastcarlocation.entity.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Contrat.
 * Long est le type de la clé primaire (numContract).
 */
@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    
    /**
     * Trouve tous les contrats avec leurs relations chargées (Agent, Client, Voiture).
     * Utilise un fetch join pour éviter les problèmes de LazyInitializationException.
     */
    @Query("SELECT DISTINCT c FROM Contrat c " +
           "LEFT JOIN FETCH c.agent " +
           "LEFT JOIN FETCH c.client " +
           "LEFT JOIN FETCH c.voiture")
    List<Contrat> findAllWithRelations();
    
    /**
     * Trouve un contrat par son ID avec ses relations chargées.
     */
    @Query("SELECT c FROM Contrat c " +
           "LEFT JOIN FETCH c.agent " +
           "LEFT JOIN FETCH c.client " +
           "LEFT JOIN FETCH c.voiture " +
           "WHERE c.numContract = :id")
    Optional<Contrat> findByIdWithRelations(@Param("id") Long id);
    
    /**
     * Trouve tous les contrats actifs pour un véhicule donné qui chevauchent avec une période donnée.
     * Un contrat est considéré comme actif s'il n'est pas terminé (la voiture est encore louée).
     * 
     * @param matricule Le matricule du véhicule
     * @param dateDebut Date de début de la location souhaitée
     * @param dateFin Date de fin de la location souhaitée
     * @return Liste des contrats qui chevauchent avec la période
     */
    @Query("SELECT c FROM Contrat c WHERE c.voiture.matricule = :matricule " +
           "AND c.dateFin >= :dateDebut AND c.dateDebut <= :dateFin")
    List<Contrat> findOverlappingContracts(
        @Param("matricule") String matricule,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin
    );
    
    /**
     * Trouve tous les contrats actifs pour un véhicule donné (excluant un contrat spécifique si fourni).
     * Utile pour la mise à jour d'un contrat existant.
     */
    @Query("SELECT c FROM Contrat c WHERE c.voiture.matricule = :matricule " +
           "AND c.dateFin >= :dateDebut AND c.dateDebut <= :dateFin " +
           "AND (:excludeId IS NULL OR c.numContract != :excludeId)")
    List<Contrat> findOverlappingContractsExcluding(
        @Param("matricule") String matricule,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin,
        @Param("excludeId") Long excludeId
    );
}