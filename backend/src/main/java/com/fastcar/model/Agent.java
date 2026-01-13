package com.fastcar.model;

import javax.persistence.*;

@Entity
@Table(name = "agent")
public class Agent {
    @Id
    @Column(name = "num_agent")
    private String numAgent;
    
    @Column(name = "nom_agent")
    private String nomAgent;
    
    @Column(name = "prenom_agent")
    private String prenomAgent;
    
    // Getters et setters
    public String getNumAgent() { return numAgent; }
    public void setNumAgent(String numAgent) { this.numAgent = numAgent; }
    
    public String getNomAgent() { return nomAgent; }
    public void setNomAgent(String nomAgent) { this.nomAgent = nomAgent; }
    
    public String getPrenomAgent() { return prenomAgent; }
    public void setPrenomAgent(String prenomAgent) { this.prenomAgent = prenomAgent; }
}