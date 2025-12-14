package models;

import java.time.LocalDate;

public class Abonne {
    private int id;
    private String cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private LocalDate dateAbonnement;

    // Constructeurs
    public Abonne() {}

    public Abonne(String cin, String nom, String prenom, String adresse, String telephone, LocalDate dateAbonnement) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.dateAbonnement = dateAbonnement;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDate getDateAbonnement() { return dateAbonnement; }
    public void setDateAbonnement(LocalDate dateAbonnement) { this.dateAbonnement = dateAbonnement; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + cin + ")";
    }
}