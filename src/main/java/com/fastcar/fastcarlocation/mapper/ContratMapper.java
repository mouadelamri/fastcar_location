package com.fastcar.fastcarlocation.mapper;

import com.fastcar.fastcarlocation.dto.ContratDTO;
import com.fastcar.fastcarlocation.dto.ContratResponseDTO;
import com.fastcar.fastcarlocation.entity.Agent;
import com.fastcar.fastcarlocation.entity.Client;
import com.fastcar.fastcarlocation.entity.Contrat;
import com.fastcar.fastcarlocation.entity.Voiture;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Contrat et les DTOs.
 */
@Component
public class ContratMapper {

    /**
     * Convertit un ContratDTO en entité Contrat.
     * Note: Les relations (Agent, Client, Voiture) doivent être chargées séparément.
     */
    public Contrat toEntity(ContratDTO dto) {
        Contrat contrat = new Contrat();
        contrat.setDateDebut(dto.getDateDebut());
        contrat.setDateFin(dto.getDateFin());
        contrat.setModePaiement(dto.getModePaiement());
        
        // Créer des objets proxy pour les relations (seront chargés par le service)
        Agent agent = new Agent();
        agent.setNumAgent(dto.getNumAgent());
        contrat.setAgent(agent);
        
        Client client = new Client();
        client.setCin(dto.getCinCli());
        contrat.setClient(client);
        
        Voiture voiture = new Voiture();
        voiture.setMatricule(dto.getMatricule());
        contrat.setVoiture(voiture);
        
        return contrat;
    }

    /**
     * Convertit une entité Contrat en ContratResponseDTO.
     * Assure que toutes les relations sont chargées avant le mapping.
     */
    public ContratResponseDTO toResponseDTO(Contrat contrat) {
        if (contrat == null) {
            return null;
        }
        
        ContratResponseDTO.ContratResponseDTOBuilder builder = ContratResponseDTO.builder()
                .numContract(contrat.getNumContract())
                .dateDebut(contrat.getDateDebut())
                .dateFin(contrat.getDateFin())
                .montantT(contrat.getMontantT())
                .modePaiement(contrat.getModePaiement());
        
        // Mapper les informations de l'agent
        if (contrat.getAgent() != null) {
            builder.numAgent(contrat.getAgent().getNumAgent())
                   .nomAgent(contrat.getAgent().getNomAgent())
                   .prenomAgent(contrat.getAgent().getPrenomAgent());
        }
        
        // Mapper les informations de la voiture
        if (contrat.getVoiture() != null) {
            builder.matricule(contrat.getVoiture().getMatricule())
                   .marque(contrat.getVoiture().getMarque())
                   .modele(contrat.getVoiture().getModele());
        }
        
        // Mapper les informations du client
        if (contrat.getClient() != null) {
            builder.cinCli(contrat.getClient().getCin())
                   .nomCli(contrat.getClient().getNomCli())
                   .prenomCli(contrat.getClient().getPrenomCli());
        }
        
        return builder.build();
    }
}

