package com.fastcar.repository;

import com.fastcar.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    
    // Trouver agent par nom ou prénom
    List<Agent> findByNomAgentContainingIgnoreCaseOrPrenomAgentContainingIgnoreCase(String nom, String prenom);
    
    // Trouver par nom
    List<Agent> findByNomAgentContainingIgnoreCase(String nom);
    
    // Trouver par prénom
    List<Agent> findByPrenomAgentContainingIgnoreCase(String prenom);
    
    // Obtenir le dernier numéro d'agent
    @Query("SELECT a FROM Agent a ORDER BY a.numAgent DESC")
    List<Agent> findAllOrderByNumAgentDesc();
    
    // Obtenir le numéro d'agent maximum
    @Query("SELECT MAX(a.numAgent) FROM Agent a")
    Optional<String> findMaxNumAgent();
    
    // Rechercher par mot-clé
    @Query("SELECT a FROM Agent a WHERE " +
           "LOWER(a.numAgent) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.nomAgent) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.prenomAgent) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Agent> search(String keyword);
}