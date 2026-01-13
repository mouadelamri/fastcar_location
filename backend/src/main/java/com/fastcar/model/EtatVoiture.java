package com.fastcar.model;

public enum EtatVoiture {
    DISPONIBLE("Disponible"),
    LOUE("Louée"),
    MAINTENANCE("En maintenance");
    
    private final String libelle;
    
    EtatVoiture(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}