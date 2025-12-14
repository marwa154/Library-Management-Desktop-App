package models;

import java.time.LocalDate;

public class Document {
    private int id;
    private String cote;
    private String titre;
    private String auteur;
    private String theme;
    private TypeDocument typeDocument;
    private AnneeEtude anneeEtude;
    private LocalDate dateParution;
    private String isbn;
    private String editeur;
    private String disponible; 
    
    public enum TypeDocument {
        OUVRAGE, MEMOIRE
    }
    
    public enum AnneeEtude {
        LICENCE, MASTER, DOCTORAT
    }
    
    // Constructeurs
    public Document() {}
    
    public Document(String cote, String titre, String auteur, String theme, 
                   TypeDocument typeDocument, AnneeEtude anneeEtude, 
                   LocalDate dateParution, String isbn, String editeur) {
        this.cote = cote;
        this.titre = titre;
        this.auteur = auteur;
        this.theme = theme;
        this.typeDocument = typeDocument;
        this.anneeEtude = anneeEtude;
        this.dateParution = dateParution;
        this.isbn = isbn;
        this.editeur = editeur;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCote() { return cote; }
    public void setCote(String cote) { this.cote = cote; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }
    
    public AnneeEtude getAnneeEtude() { return anneeEtude; }
    public void setAnneeEtude(AnneeEtude anneeEtude) { this.anneeEtude = anneeEtude; }
    
    public LocalDate getDateParution() { return dateParution; }
    public void setDateParution(LocalDate dateParution) { this.dateParution = dateParution; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }
    
    // Ajoutez ces getter et setter pour la disponibilité
    public String getDisponible() { return disponible; }
    public void setDisponible(String disponible) { this.disponible = disponible; }
}