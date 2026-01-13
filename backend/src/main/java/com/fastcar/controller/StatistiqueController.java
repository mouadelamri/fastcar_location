package com.fastcar.controller;

import com.fastcar.repository.VoitureRepository;
import com.fastcar.repository.ClientRepository;
import com.fastcar.repository.AgentRepository;
import com.fastcar.repository.ContratRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
@CrossOrigin(origins = "*")
public class StatistiqueController {
    
    @Autowired
    private VoitureRepository voitureRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private ContratRepository contratRepository;
    
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques des voitures
        long totalVoitures = voitureRepository.count();
        long voituresDisponibles = voitureRepository.countByEtatVeh("Disponible");
        long voituresLouees = voitureRepository.countByEtatVeh("Louée");
        long voituresMaintenance = voitureRepository.countByEtatVeh("En maintenance");
        
        stats.put("totalVoitures", totalVoitures);
        stats.put("voituresDisponibles", voituresDisponibles);
        stats.put("voituresLouees", voituresLouees);
        stats.put("voituresMaintenance", voituresMaintenance);
        
        // Statistiques générales
        stats.put("totalClients", clientRepository.count());
        stats.put("totalAgents", agentRepository.count());
        stats.put("totalContrats", contratRepository.count());
        
        // Calculer le taux d'occupation
        double tauxOccupation = totalVoitures > 0 ? 
            Math.round((voituresLouees * 100.0) / totalVoitures) : 0;
        stats.put("tauxOccupation", tauxOccupation);
        
        // Contrats du mois
        LocalDate debutMois = LocalDate.now().withDayOfMonth(1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());
        long contratsCeMois = contratRepository.countByDateDebutBetween(debutMois, finMois);
        stats.put("contratsCeMois", contratsCeMois);
        
        // Revenu estimé ce mois
        double revenuMensuel = contratsCeMois * 500.0; // Estimation moyenne
        stats.put("revenuEstime", revenuMensuel);
        
        return stats;
    }
    
    @GetMapping("/details")
    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Ajouter des statistiques détaillées
        stats.put("voituresParMarque", getVoituresParMarque());
        stats.put("contratsParMois", getContratsParMois());
        stats.put("clientsNouveaux", getNouveauxClients());
        
        return stats;
    }
    
    private Map<String, Long> getVoituresParMarque() {
        // Cette méthode devrait implémenter une logique pour grouper par marque
        Map<String, Long> result = new HashMap<>();
        result.put("Dacia", 5L);
        result.put("Renault", 3L);
        result.put("Peugeot", 2L);
        return result;
    }
    
    private Map<String, Long> getContratsParMois() {
        Map<String, Long> result = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            result.put("Mois-" + i, (long) (Math.random() * 10));
        }
        return result;
    }
    
    private Long getNouveauxClients() {
        // Retourner le nombre de nouveaux clients ce mois
        return 12L;
    }
}