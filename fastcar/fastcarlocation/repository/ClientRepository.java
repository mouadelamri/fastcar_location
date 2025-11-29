package com.fastcar.fastcarlocation.repository;

import com.fastcar.fastcarlocation.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Client.
 * String est le type de la clé primaire (cin).
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
}