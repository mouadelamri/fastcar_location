package com.fastcar.controller;

import com.fastcar.model.Contrat;
import com.fastcar.model.Voiture;
import com.fastcar.repository.ContratRepository;
import com.fastcar.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contrats")
@CrossOrigin(origins = "*")
public class ContratController {
    
    @Autowired
    private ContratRepository contratRepository;
    
    @Autowired
    private VoitureRepository voitureRepository;
    
    // GET tous les contrats
    @GetMapping
    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }
    
    // GET contrat par numéro
    @GetMapping("/{numContrat}")
    public ResponseEntity<Contrat> getContratByNumero(@PathVariable String numContrat) {
        Optional<Contrat> contrat = contratRepository.findById(numContrat);
        return contrat.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // GET prochain numéro de contrat
    @GetMapping("/next-numero")
    public ResponseEntity<Map<String, String>> getNextContratNumber() {
        // Compter les contrats pour générer un numéro séquentiel
        long count = contratRepository.count();
        
        // Format: LOC-YYYY-XXXXX (ex: LOC-2024-00001)
        String annee = String.valueOf(LocalDate.now().getYear());
        String sequence = String.format("%05d", count + 1);
        
        String nextNumero = "LOC-" + annee + "-" + sequence;
        
        Map<String, String> response = new HashMap<>();
        response.put("nextNumero", nextNumero);
        return ResponseEntity.ok(response);
    }
    
    // POST créer un contrat
    @PostMapping
    public ResponseEntity<?> createContrat(@RequestBody Contrat contrat) {
        try {
            // Vérifier si le contrat existe déjà
            if (contratRepository.existsById(contrat.getNumContrat())) {
                return ResponseEntity.badRequest().body("Numéro de contrat déjà existant");
            }
            
            // Vérifier si la voiture existe
            Optional<Voiture> voiture = voitureRepository.findById(contrat.getMatriculeFk());
            if (voiture.isEmpty()) {
                return ResponseEntity.badRequest().body("Voiture non trouvée");
            }
            
            // Vérifier si la voiture est disponible
            if (!"Disponible".equals(voiture.get().getEtatVeh())) {
                return ResponseEntity.badRequest().body("La voiture n'est pas disponible");
            }
            
            // Sauvegarder le contrat
            Contrat savedContrat = contratRepository.save(contrat);
            
            // Mettre à jour l'état de la voiture à "Louée"
            Voiture voitureEntity = voiture.get();
            voitureEntity.setEtatVeh("Louée");
            voitureRepository.save(voitureEntity);
            
            return ResponseEntity.ok(savedContrat);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création du contrat: " + e.getMessage());
        }
    }
    
    // PUT modifier un contrat
    @PutMapping("/{numContrat}")
    public ResponseEntity<Contrat> updateContrat(@PathVariable String numContrat, 
                                                 @RequestBody Contrat contratDetails) {
        Optional<Contrat> optionalContrat = contratRepository.findById(numContrat);
        
        if (optionalContrat.isPresent()) {
            Contrat contrat = optionalContrat.get();
            
            contrat.setDateDebut(contratDetails.getDateDebut());
            contrat.setDateFin(contratDetails.getDateFin());
            contrat.setMontantT(contratDetails.getMontantT());
            contrat.setModePaiement(contratDetails.getModePaiement());
            contrat.setNumAgentFk(contratDetails.getNumAgentFk());
            contrat.setMatriculeFk(contratDetails.getMatriculeFk());
            contrat.setCinCliFk(contratDetails.getCinCliFk());
            
            Contrat updatedContrat = contratRepository.save(contrat);
            return ResponseEntity.ok(updatedContrat);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // DELETE supprimer un contrat
    @DeleteMapping("/{numContrat}")
    public ResponseEntity<Map<String, Boolean>> deleteContrat(@PathVariable String numContrat) {
        Optional<Contrat> contrat = contratRepository.findById(numContrat);
        
        if (contrat.isPresent()) {
            // Récupérer la voiture associée
            String matricule = contrat.get().getMatriculeFk();
            Optional<Voiture> voiture = voitureRepository.findById(matricule);
            
            // Mettre à jour l'état de la voiture à "Disponible"
            if (voiture.isPresent()) {
                Voiture voitureEntity = voiture.get();
                voitureEntity.setEtatVeh("Disponible");
                voitureRepository.save(voitureEntity);
            }
            
            // Supprimer le contrat
            contratRepository.delete(contrat.get());
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // GET contrats par date
    @GetMapping("/date/{date}")
    public List<Contrat> getContratsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return contratRepository.findByDateDebutOrDateFin(date, date);
    }
    
    // GET contrats par client
    @GetMapping("/client/{cin}")
    public List<Contrat> getContratsByClient(@PathVariable String cin) {
        return contratRepository.findByCinCliFk(cin);
    }
    
    // GET contrats par voiture
    @GetMapping("/voiture/{matricule}")
    public List<Contrat> getContratsByVoiture(@PathVariable String matricule) {
        return contratRepository.findByMatriculeFk(matricule);
    }
    
    // GET contrats par agent
    @GetMapping("/agent/{numAgent}")
    public List<Contrat> getContratsByAgent(@PathVariable String numAgent) {
        return contratRepository.findByNumAgentFk(numAgent);
    }
    
    // GET contrats actifs (en cours)
    @GetMapping("/actifs")
    public List<Contrat> getContratsActifs() {
        LocalDate aujourdhui = LocalDate.now();
        return contratRepository.findByDateDebutLessThanEqualAndDateFinGreaterThanEqual(aujourdhui, aujourdhui);
    }
    
    // GET rechercher des contrats
    @GetMapping("/search")
    public List<Contrat> searchContrats(@RequestParam String keyword) {
        return contratRepository.search(keyword);
    }
    
    // GET statistiques des contrats
    @GetMapping("/statistiques")
    public Map<String, Object> getStatistiquesContrats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", contratRepository.count());
        
        // Compter les contrats par mois de l'année en cours
        LocalDate maintenant = LocalDate.now();
        int annee = maintenant.getYear();
        
        for (int mois = 1; mois <= 12; mois++) {
            LocalDate debutMois = LocalDate.of(annee, mois, 1);
            LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());
            
            long count = contratRepository.countByDateDebutBetween(debutMois, finMois);
            stats.put("mois_" + mois, count);
        }
        
        return stats;
    }
}