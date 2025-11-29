package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.exception.EntityNotFoundException;
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

    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<Client> consulterClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(String cin) {
        return clientRepository.findById(cin);
    }

    /**
     * Récupère un client par son CIN ou lance une exception si non trouvé.
     */
    @Transactional(readOnly = true)
    public Client findByIdOrThrow(String cin) {
        return clientRepository.findById(cin)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec le CIN: " + cin));
    }

    public void deleteById(String cin) {
        if (!clientRepository.existsById(cin)) {
            throw new EntityNotFoundException("Client non trouvé avec le CIN: " + cin);
        }
        clientRepository.deleteById(cin);
    }

    public void supprimerClient(String cin) {
        deleteById(cin);
    }
}