package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier de l'entité Client.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public Client save(Client client) {
        // Logique métier : S'assurer que le format du CIN est correct, etc.
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(String cin) {
        return clientRepository.findById(cin);
    }

    public void deleteById(String cin) {
        clientRepository.deleteById(cin);
    }
}