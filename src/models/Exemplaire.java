package models;

import java.time.LocalDate;

public class Exemplaire {
    private int id;
    private int documentId;
    private String libelle;
    private String localisation;
    private LocalDate dateAcquisition;
    private Statut statut;
    
    public enum Statut {
        DISPONIBLE, EMPRUNTE
    }
    
    public Exemplaire() {}
    
    public Exemplaire(int documentId, String libelle, String localisation, 
                     LocalDate dateAcquisition, Statut statut) {
        this.documentId = documentId;
        this.libelle = libelle;
        this.localisation = localisation;
        this.dateAcquisition = dateAcquisition;
        this.statut = statut;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getDocumentId() { return documentId; }
    public void setDocumentId(int documentId) { this.documentId = documentId; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    
    public LocalDate getDateAcquisition() { return dateAcquisition; }
    public void setDateAcquisition(LocalDate dateAcquisition) { this.dateAcquisition = dateAcquisition; }
    
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
}