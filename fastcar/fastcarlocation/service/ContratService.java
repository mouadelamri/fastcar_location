package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.entity.Contrat;
import com.fastcar.fastcarlocation.entity.Voiture;
import com.fastcar.fastcarlocation.enums.EtatVehicule;
import com.fastcar.fastcarlocation.repository.ContratRepository;
import com.fastcar.fastcarlocation.repository.AgentRepository;
import com.fastcar.fastcarlocation.repository.ClientRepository;
import com.fastcar.fastcarlocation.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier complexe des Contrats.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ContratService {

    private final ContratRepository contratRepository;
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final VoitureRepository voitureRepository;

    /**
     * Enregistre un nouveau contrat après validation et calculs.
     */
    public Contrat createContract(Contrat contrat) {
        // 1. Récupération et vérification des entités existantes (clés étrangères)
        Agent agent = agentRepository.findById(contrat.getAgent().getNumAgent())
                .orElseThrow(() -> new RuntimeException("Agent non trouvé."));
        Client client = clientRepository.findById(contrat.getClient().getCin())
                .orElseThrow(() -> new RuntimeException("Client non trouvé."));
        Voiture voiture = voitureRepository.findById(contrat.getVoiture().getMatricule())
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée."));

        // Vérification de l'état (Logique Métier : ne pas louer si non disponible)
        if (voiture.getEtatVeh() != EtatVehicule.DISPONIBLE) {
            throw new RuntimeException("La voiture n'est pas disponible pour la location.");
        }

        // 2. Calcul du Montant Total (Logique Métier)
        long jours = ChronoUnit.DAYS.between(contrat.getDateDebut(), contrat.getDateFin()) + 1; // +1 pour inclure le jour de fin
        if (jours <= 0) {
            throw new RuntimeException("La durée de location doit être d'au moins un jour.");
        }
        
        BigDecimal prixJournalier = voiture.getPrixLoc(); [cite_start]// Prix/jour du véhicule [cite: 27]
        BigDecimal montantTotal = prixJournalier.multiply(BigDecimal.valueOf(jours));

        // Mise à jour de l'objet contrat avec les bonnes relations et le montant calculé
        contrat.setAgent(agent);
        contrat.setClient(client);
        contrat.setVoiture(voiture);
        contrat.setMontantT(montantTotal);

        // 3. Mise à jour de l'état du véhicule (Logique Métier)
        voiture.setEtatVeh(EtatVehicule.LOUEE);
        voitureRepository.save(voiture); // Sauvegarde de la voiture avec son nouvel état

        // 4. Sauvegarde du contrat
        return contratRepository.save(contrat);
    }

    /**
     * Récupère tous les contrats.
     */
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    /**
     * Récupère un contrat par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Contrat> findById(Long id) {
        return contratRepository.findById(id);
    }

    /**
     * Termine la location (met la voiture en DISPONIBLE).
     */
    public void endContract(Long contractId) {
        Contrat contrat = contratRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé."));
        
        Voiture voiture = contrat.getVoiture();
        
        if (voiture.getEtatVeh() == EtatVehicule.LOUEE) {
            voiture.setEtatVeh(EtatVehicule.DISPONIBLE);
            voitureRepository.save(voiture);
        }
        // Généralement, on ne supprime pas le contrat, on marque sa fin ou on archive,
        // mais pour ce petit projet, on laisse la méthode delete standard.
    }
}