package com.fastcar.repository;

import com.fastcar.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    // Trouver par nom (insensible à la casse)
    List<Client> findByNomCliContainingIgnoreCase(String nom);
    
    // Trouver par prénom (insensible à la casse)
    List<Client> findByPrenomCliContainingIgnoreCase(String prenom);
    
    // Trouver par nom ou prénom
    List<Client> findByNomCliContainingIgnoreCaseOrPrenomCliContainingIgnoreCase(String nom, String prenom);
    
    // Trouver par téléphone
    List<Client> findByTelephoneContaining(String telephone);
    
    // Trouver par email
    List<Client> findByEmailContainingIgnoreCase(String email);
    
    // Recherche générale
    @Query("SELECT c FROM Client c WHERE " +
           "LOWER(c.cinCli) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.nomCli) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.prenomCli) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.telephone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Client> search(String keyword);
}