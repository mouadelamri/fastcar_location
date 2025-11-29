package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.entity.Contrat;
import com.fastcar.fastcarlocation.entity.Voiture;
import com.fastcar.fastcarlocation.enums.EtatVehicule;
import com.fastcar.fastcarlocation.exception.DateOverlapException;
import com.fastcar.fastcarlocation.exception.EntityNotFoundException;
import com.fastcar.fastcarlocation.exception.VehiculeNotAvailableException;
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
     * Vérifie les règles métier :
     * - Les entités (Agent, Client, Voiture) doivent exister
     * - La voiture doit être disponible
     * - Il ne doit pas y avoir de chevauchement de dates avec d'autres contrats
     */
    public Contrat createContract(Contrat contrat) {
        // 1. Récupération et vérification des entités existantes (clés étrangères)
        Agent agent = agentRepository.findById(contrat.getAgent().getNumAgent())
                .orElseThrow(() -> new EntityNotFoundException("Agent non trouvé avec l'ID: " + contrat.getAgent().getNumAgent()));
        Client client = clientRepository.findById(contrat.getClient().getCin())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec le CIN: " + contrat.getClient().getCin()));
        Voiture voiture = voitureRepository.findById(contrat.getVoiture().getMatricule())
                .orElseThrow(() -> new EntityNotFoundException("Voiture non trouvée avec le matricule: " + contrat.getVoiture().getMatricule()));

        // 2. Vérification de l'état (Logique Métier : ne pas louer si non disponible)
        if (voiture.getEtatVeh() != EtatVehicule.DISPONIBLE) {
            throw new VehiculeNotAvailableException(
                "La voiture " + voiture.getMatricule() + " n'est pas disponible pour la location. État actuel: " + voiture.getEtatVeh()
            );
        }

        // 3. Vérification des dates
        if (contrat.getDateDebut().isAfter(contrat.getDateFin())) {
            throw new DateOverlapException("La date de début doit être antérieure ou égale à la date de fin.");
        }

        // 4. Vérification des chevauchements de dates (Règle métier : une voiture ne peut être louée qu'une seule fois à un instant donné)
        List<Contrat> overlappingContracts = contratRepository.findOverlappingContracts(
            voiture.getMatricule(),
            contrat.getDateDebut(),
            contrat.getDateFin()
        );
        
        if (!overlappingContracts.isEmpty()) {
            throw new DateOverlapException(
                "La voiture " + voiture.getMatricule() + " est déjà louée pendant cette période. " +
                "Contrats existants: " + overlappingContracts.size()
            );
        }

        // 5. Calcul du Montant Total (Logique Métier)
        long jours = ChronoUnit.DAYS.between(contrat.getDateDebut(), contrat.getDateFin()) + 1; // +1 pour inclure le jour de fin
        if (jours <= 0) {
            throw new DateOverlapException("La durée de location doit être d'au moins un jour.");
        }
        
        BigDecimal prixJournalier = voiture.getPrixLoc(); // Prix/jour du véhicule
        BigDecimal montantTotal = prixJournalier.multiply(BigDecimal.valueOf(jours));

        // 6. Mise à jour de l'objet contrat avec les bonnes relations et le montant calculé
        contrat.setAgent(agent);
        contrat.setClient(client);
        contrat.setVoiture(voiture);
        contrat.setMontantT(montantTotal);

        // 7. Mise à jour de l'état du véhicule (Logique Métier)
        voiture.setEtatVeh(EtatVehicule.LOUEE);
        voitureRepository.save(voiture); // Sauvegarde de la voiture avec son nouvel état

        // 8. Sauvegarde du contrat
        return contratRepository.save(contrat);
    }

    /**
     * Récupère tous les contrats avec leurs relations chargées.
     */
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAllWithRelations();
    }

    /**
     * Récupère un contrat par son ID avec ses relations chargées.
     */
    @Transactional(readOnly = true)
    public Optional<Contrat> findById(Long id) {
        return contratRepository.findByIdWithRelations(id);
    }

    /**
     * Récupère un contrat par son ID ou lance une exception si non trouvé.
     */
    @Transactional(readOnly = true)
    public Contrat findByIdOrThrow(Long id) {
        return contratRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat non trouvé avec l'ID: " + id));
    }

    /**
     * Termine la location (met la voiture en DISPONIBLE).
     */
    public void endContract(Long contractId) {
        Contrat contrat = findByIdOrThrow(contractId);
        
        Voiture voiture = contrat.getVoiture();
        
        if (voiture.getEtatVeh() == EtatVehicule.LOUEE) {
            voiture.setEtatVeh(EtatVehicule.DISPONIBLE);
            voitureRepository.save(voiture);
        }
        // Généralement, on ne supprime pas le contrat, on marque sa fin ou on archive,
        // mais pour ce petit projet, on laisse la méthode delete standard.
    }

    /**
     * Supprime un contrat par son ID.
     * Remet la voiture en DISPONIBLE si elle était louée.
     */
    public void deleteById(Long id) {
        Contrat contrat = findByIdOrThrow(id);
        Voiture voiture = contrat.getVoiture();
        
        // Remettre la voiture disponible si elle était louée
        if (voiture.getEtatVeh() == EtatVehicule.LOUEE) {
            voiture.setEtatVeh(EtatVehicule.DISPONIBLE);
            voitureRepository.save(voiture);
        }
        
        contratRepository.deleteById(id);
    }

    public Contrat ajouterContrat(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    public List<Contrat> consulterContrats() {
        return contratRepository.findAll();
    }

    public void supprimerContrat(Long numContract) {
        contratRepository.deleteById(numContract);
    }
}