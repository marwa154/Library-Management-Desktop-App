package dao;

import config.DatabaseConnection;
import models.Abonne;
import models.Document;
import models.Exemplaire;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    
    public void addDocument(Document document) throws SQLException {
        String sql = "INSERT INTO documents (cote, titre, auteur, theme, type_document, annee_etude, date_parution, isbn, editeur) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, document.getCote());
            stmt.setString(2, document.getTitre());
            stmt.setString(3, document.getAuteur());
            stmt.setString(4, document.getTheme());
            stmt.setString(5, document.getTypeDocument().name());
            stmt.setString(6, document.getAnneeEtude() != null ? document.getAnneeEtude().name() : null);
            stmt.setDate(7, document.getDateParution() != null ? Date.valueOf(document.getDateParution()) : null);
            stmt.setString(8, document.getIsbn());
            stmt.setString(9, document.getEditeur());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    document.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    private String calculerDisponibilite(int documentId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM exemplaires WHERE document_id = ? AND statut = 'DISPONIBLE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0 ? "disponible" : "emprunté";
                }
            }
        }
        return "emprunté";
    }
    
    public List<Document> getAllDocuments() throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Document doc = new Document();
                doc.setId(rs.getInt("id"));
                doc.setCote(rs.getString("cote"));
                doc.setTitre(rs.getString("titre"));
                doc.setAuteur(rs.getString("auteur"));
                doc.setTheme(rs.getString("theme"));
                doc.setTypeDocument(Document.TypeDocument.valueOf(rs.getString("type_document")));
                
                String anneeEtude = rs.getString("annee_etude");
                if (anneeEtude != null) {
                    doc.setAnneeEtude(Document.AnneeEtude.valueOf(anneeEtude));
                }
                
                Date dateParution = rs.getDate("date_parution");
                if (dateParution != null) {
                    doc.setDateParution(dateParution.toLocalDate());
                }
                
                doc.setIsbn(rs.getString("isbn"));
                doc.setEditeur(rs.getString("editeur"));
                
                // Calculer la disponibilité
                String disponibilite = calculerDisponibilite(doc.getId());
                doc.setDisponible(disponibilite);
                
                documents.add(doc);
            }
        }
        return documents;
    }
    
    public void updateDocument(Document document) throws SQLException {
        String sql = "UPDATE documents SET cote=?, titre=?, auteur=?, theme=?, type_document=?, " +
                    "annee_etude=?, date_parution=?, isbn=?, editeur=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, document.getCote());
            stmt.setString(2, document.getTitre());
            stmt.setString(3, document.getAuteur());
            stmt.setString(4, document.getTheme());
            stmt.setString(5, document.getTypeDocument().name());
            stmt.setString(6, document.getAnneeEtude() != null ? document.getAnneeEtude().name() : null);
            stmt.setDate(7, document.getDateParution() != null ? Date.valueOf(document.getDateParution()) : null);
            stmt.setString(8, document.getIsbn());
            stmt.setString(9, document.getEditeur());
            stmt.setInt(10, document.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteDocument(int id) throws SQLException {
        String sql = "DELETE FROM documents WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void deleteExemplaire(int exemplaireId) throws SQLException {
        String sql = "DELETE FROM exemplaires WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exemplaireId);
            stmt.executeUpdate();
        }
    }
    public void addExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "INSERT INTO exemplaires (document_id, libelle, localisation, date_acquisition, statut) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exemplaire.getDocumentId());
            stmt.setString(2, exemplaire.getLibelle());
            stmt.setString(3, exemplaire.getLocalisation());
            stmt.setDate(4, Date.valueOf(exemplaire.getDateAcquisition()));
            stmt.setString(5, exemplaire.getStatut().name());
            
            stmt.executeUpdate();
        }
    }
    
    
    public List<Exemplaire> getExemplairesByDocument(int documentId) throws SQLException {
        List<Exemplaire> exemplaires = new ArrayList<>();
        String sql = "SELECT * FROM exemplaires WHERE document_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exemplaire ex = new Exemplaire();
                    ex.setId(rs.getInt("id"));
                    ex.setDocumentId(rs.getInt("document_id"));
                    ex.setLibelle(rs.getString("libelle"));
                    ex.setLocalisation(rs.getString("localisation"));
                    ex.setDateAcquisition(rs.getDate("date_acquisition").toLocalDate());
                    ex.setStatut(Exemplaire.Statut.valueOf(rs.getString("statut")));
                    
                    exemplaires.add(ex);
                }
            }
        }
        return exemplaires;
    }
}