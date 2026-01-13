package com.fastcar.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contrat")
public class Contrat {
    @Id
    @Column(name = "num_contrat")
    private String numContrat;
    
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    @Column(name = "montant_t")
    private double montantT;
    
    @Column(name = "mode_paiement")
    private String modePaiement;
    
    @Column(name = "num_agent_fk")
    private String numAgentFk;
    
    @Column(name = "matricule_fk")
    private String matriculeFk;
    
    @Column(name = "cin_cli_fk")
    private String cinCliFk;
    
    // Getters et setters
    public String getNumContrat() { return numContrat; }
    public void setNumContrat(String numContrat) { this.numContrat = numContrat; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public double getMontantT() { return montantT; }
    public void setMontantT(double montantT) { this.montantT = montantT; }
    
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    
    public String getNumAgentFk() { return numAgentFk; }
    public void setNumAgentFk(String numAgentFk) { this.numAgentFk = numAgentFk; }
    
    public String getMatriculeFk() { return matriculeFk; }
    public void setMatriculeFk(String matriculeFk) { this.matriculeFk = matriculeFk; }
    
    public String getCinCliFk() { return cinCliFk; }
    public void setCinCliFk(String cinCliFk) { this.cinCliFk = cinCliFk; }
}