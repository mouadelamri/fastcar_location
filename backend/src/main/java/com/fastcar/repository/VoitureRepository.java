package com.fastcar.repository;

import com.fastcar.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, String> {
    
    // Compter les voitures par état
    long countByEtatVeh(String etatVeh);
    
    // Trouver par marque (recherche insensible à la casse)
    List<Voiture> findByMarqueContainingIgnoreCase(String marque);
    
    // Trouver par état
    List<Voiture> findByEtatVeh(String etat);
    
    // Trouver les voitures disponibles
    @Query("SELECT v FROM Voiture v WHERE v.etatVeh = 'Disponible'")
    List<Voiture> findVoituresDisponibles();
    
    // Trouver les voitures louées
    @Query("SELECT v FROM Voiture v WHERE v.etatVeh = 'Louée'")
    List<Voiture> findVoituresLouees();
    
    // Rechercher par matricule, marque ou modèle
    @Query("SELECT v FROM Voiture v WHERE " +
           "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.marque) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.modele) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Voiture> search(String keyword);
}