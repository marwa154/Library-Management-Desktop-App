package models;

import java.time.LocalDate;

public class Pret
{
	private int id; private int abonneId; 
private int documentId;
private String abonne;

private String document; 
private LocalDate datePret; 
private LocalDate dateRetourPrevu; 
private LocalDate dateRetourEffective;
private String statut; 

public Pret(int id, int abonneId, int documentId, String abonne, String document, LocalDate datePret,
		LocalDate dateRetourPrevu, LocalDate dateRetourEffective, String statut)
{ this.id = id; 
this.abonneId = abonneId; this.documentId = documentId; this.abonne = abonne;
this.document = document; this.datePret = datePret; this.dateRetourPrevu = dateRetourPrevu; 
this.dateRetourEffective = dateRetourEffective; this.statut = statut; } 

public int getId() 
{ return id; } 
public int getAbonneId() { return abonneId; }
public int getDocumentId() { return documentId; } public String getAbonne() 
{ return abonne; } public String getDocument() { return document; } public LocalDate getDatePret()
{ return datePret; } public LocalDate getDateRetourPrevu() { return dateRetourPrevu; }
public LocalDate getDateRetourEffective() { return dateRetourEffective; }
public String getStatut() { return statut; } 

public void setAbonneId(int abonneId) { this.abonneId = abonneId; } 
public void setDocumentId(int documentId) { this.documentId = documentId; }
public void setAbonne(String abonne) { this.abonne = abonne; } public void setDocument(String document) 
{ this.document = document; } public void setDatePret(LocalDate datePret) { this.datePret = datePret; }
public void setDateRetourPrevu(LocalDate dateRetourPrevu) { this.dateRetourPrevu = dateRetourPrevu; }
public void setDateRetourEffective(LocalDate dateRetourEffective) 
{ this.dateRetourEffective = dateRetourEffective; } 
public void setStatut(String statut)
{ this.statut = statut; } 
}
