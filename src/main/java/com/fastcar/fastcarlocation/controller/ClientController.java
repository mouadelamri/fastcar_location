package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des Clients.
 * Les endpoints sont préfixés par /api/clients.
 */
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // POST /api/clients
    // Crée un nouveau client
    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        // @Valid déclenche la validation des contraintes définies dans l'entité Client (NotBlank, Email, etc.)
        Client savedClient = clientService.save(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // GET /api/clients
    // Récupère la liste de tous les clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }

    // GET /api/clients/{cin}
    // Récupère un client par son CIN (clé primaire)
    @GetMapping("/{cin}")
    public ResponseEntity<Client> getClientByCin(@PathVariable String cin) {
        return clientService.findById(cin)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/clients/{cin}
    // Met à jour un client existant
    @PutMapping("/{cin}")
    public ResponseEntity<Client> updateClient(@PathVariable String cin, @Valid @RequestBody Client clientDetails) {
        return clientService.findById(cin)
                .map(existingClient -> {
                    // Mise à jour des champs
                    existingClient.setNomCli(clientDetails.getNomCli());
                    existingClient.setPrenomCli(clientDetails.getPrenomCli());
                    existingClient.setAdresse(clientDetails.getAdresse());
                    existingClient.setTelephone(clientDetails.getTelephone());
                    existingClient.setEmail(clientDetails.getEmail());

                    Client updatedClient = clientService.save(existingClient);
                    return ResponseEntity.ok(updatedClient);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/clients/{cin}
    // Supprime un client
    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteClient(@PathVariable String cin) {
        if (clientService.findById(cin).isPresent()) {
            clientService.deleteById(cin);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}