package com.fastcar.controller;

import com.fastcar.model.Client;
import com.fastcar.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {
    
    @Autowired
    private ClientRepository clientRepository;
    
    // GET tous les clients
    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    // GET client par CIN
    @GetMapping("/{cin}")
    public ResponseEntity<Client> getClientByCin(@PathVariable String cin) {
        Optional<Client> client = clientRepository.findById(cin);
        return client.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    // POST ajouter un client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        // Vérifier si le CIN existe déjà
        if (clientRepository.existsById(client.getCinCli())) {
            return ResponseEntity.badRequest().body(null);
        }
        
        Client savedClient = clientRepository.save(client);
        return ResponseEntity.ok(savedClient);
    }
    
    // PUT modifier un client
    @PutMapping("/{cin}")
    public ResponseEntity<Client> updateClient(@PathVariable String cin, 
                                               @RequestBody Client clientDetails) {
        Optional<Client> optionalClient = clientRepository.findById(cin);
        
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            
            client.setNomCli(clientDetails.getNomCli());
            client.setPrenomCli(clientDetails.getPrenomCli());
            client.setAdresse(clientDetails.getAdresse());
            client.setTelephone(clientDetails.getTelephone());
            client.setEmail(clientDetails.getEmail());
            
            Client updatedClient = clientRepository.save(client);
            return ResponseEntity.ok(updatedClient);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // PATCH modifier seulement certaines informations
    @PatchMapping("/{cin}")
    public ResponseEntity<Client> partialUpdateClient(@PathVariable String cin, 
                                                      @RequestBody Map<String, Object> updates) {
        Optional<Client> optionalClient = clientRepository.findById(cin);
        
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            
            updates.forEach((key, value) -> {
                switch (key) {
                    case "nomCli":
                        client.setNomCli((String) value);
                        break;
                    case "prenomCli":
                        client.setPrenomCli((String) value);
                        break;
                    case "adresse":
                        client.setAdresse((String) value);
                        break;
                    case "telephone":
                        client.setTelephone((String) value);
                        break;
                    case "email":
                        client.setEmail((String) value);
                        break;
                }
            });
            
            Client updatedClient = clientRepository.save(client);
            return ResponseEntity.ok(updatedClient);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // DELETE supprimer un client
    @DeleteMapping("/{cin}")
    public ResponseEntity<Map<String, Boolean>> deleteClient(@PathVariable String cin) {
        Optional<Client> client = clientRepository.findById(cin);
        
        if (client.isPresent()) {
            clientRepository.delete(client.get());
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // GET rechercher des clients
    @GetMapping("/search")
    public List<Client> searchClients(@RequestParam String keyword) {
        return clientRepository.search(keyword);
    }
    
    // GET clients par nom
    @GetMapping("/search/nom")
    public List<Client> searchClientsByNom(@RequestParam String nom) {
        return clientRepository.findByNomCliContainingIgnoreCase(nom);
    }
    
    // GET clients par prénom
    @GetMapping("/search/prenom")
    public List<Client> searchClientsByPrenom(@RequestParam String prenom) {
        return clientRepository.findByPrenomCliContainingIgnoreCase(prenom);
    }
    
    // GET compter les clients
    @GetMapping("/count")
    public Map<String, Long> countClients() {
        Map<String, Long> response = new HashMap<>();
        response.put("total", clientRepository.count());
        return response;
    }
    
    // GET vérifier si CIN existe
    @GetMapping("/exists/{cin}")
    public ResponseEntity<Map<String, Boolean>> checkCinExists(@PathVariable String cin) {
        boolean exists = clientRepository.existsById(cin);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}