package com.fastcar.repository;

import com.fastcar.model.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, String> {
    
    // Trouver par client
    List<Contrat> findByCinCliFk(String cinCliFk);
    
    // Trouver par voiture
    List<Contrat> findByMatriculeFk(String matriculeFk);
    
    // Trouver par agent
    List<Contrat> findByNumAgentFk(String numAgentFk);
    
    // Trouver par date de début ou fin
    List<Contrat> findByDateDebutOrDateFin(LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les contrats actifs
    List<Contrat> findByDateDebutLessThanEqualAndDateFinGreaterThanEqual(LocalDate date1, LocalDate date2);
    
    // Compter les contrats entre deux dates
    long countByDateDebutBetween(LocalDate start, LocalDate end);
    
    // Rechercher par mot-clé
    @Query("SELECT c FROM Contrat c WHERE " +
           "LOWER(c.numContrat) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.cinCliFk) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.matriculeFk) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.numAgentFk) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Contrat> search(String keyword);
    
    // Trouver les contrats avec détails (jointure)
    @Query("SELECT c FROM Contrat c ORDER BY c.dateDebut DESC")
    List<Contrat> findAllOrderByDateDebutDesc();
}