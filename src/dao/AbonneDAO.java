package dao;

import config.DatabaseConnection;
import models.Abonne;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbonneDAO {
    
    public void addAbonne(Abonne abonne) throws SQLException {
        String sql = "INSERT INTO abonnes (cin, nom, prenom, adresse, telephone, date_abonnement) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, abonne.getCin());
            stmt.setString(2, abonne.getNom());
            stmt.setString(3, abonne.getPrenom());
            stmt.setString(4, abonne.getAdresse());
            stmt.setString(5, abonne.getTelephone());
            stmt.setDate(6, Date.valueOf(abonne.getDateAbonnement()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    abonne.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public List<Abonne> getAllAbonnes() throws SQLException {
        List<Abonne> abonnes = new ArrayList<>();
        String sql = "SELECT * FROM abonnes ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Abonne abonne = new Abonne();
                abonne.setId(rs.getInt("id"));
                abonne.setCin(rs.getString("cin"));
                abonne.setNom(rs.getString("nom"));
                abonne.setPrenom(rs.getString("prenom"));
                abonne.setAdresse(rs.getString("adresse"));
                abonne.setTelephone(rs.getString("telephone"));
                
                Date dateAbonnement = rs.getDate("date_abonnement");
                if (dateAbonnement != null) {
                    abonne.setDateAbonnement(dateAbonnement.toLocalDate());
                }
                
                abonnes.add(abonne);
            }
        }
        return abonnes;
    }
    
    public void updateAbonne(Abonne abonne) throws SQLException {
        String sql = "UPDATE abonnes SET cin=?, nom=?, prenom=?, adresse=?, telephone=?, date_abonnement=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, abonne.getCin());
            stmt.setString(2, abonne.getNom());
            stmt.setString(3, abonne.getPrenom());
            stmt.setString(4, abonne.getAdresse());
            stmt.setString(5, abonne.getTelephone());
            stmt.setDate(6, Date.valueOf(abonne.getDateAbonnement()));
            stmt.setInt(7, abonne.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteAbonne(int id) throws SQLException {
        String sql = "DELETE FROM abonnes WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public Abonne getAbonneById(int id) throws SQLException {
        String sql = "SELECT * FROM abonnes WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Abonne abonne = new Abonne();
                    abonne.setId(rs.getInt("id"));
                    abonne.setCin(rs.getString("cin"));
                    abonne.setNom(rs.getString("nom"));
                    abonne.setPrenom(rs.getString("prenom"));
                    abonne.setAdresse(rs.getString("adresse"));
                    abonne.setTelephone(rs.getString("telephone"));
                    
                    Date dateAbonnement = rs.getDate("date_abonnement");
                    if (dateAbonnement != null) {
                        abonne.setDateAbonnement(dateAbonnement.toLocalDate());
                    }
                    
                    return abonne;
                }
            }
        }
        return null;
    }
    
    public List<Abonne> searchAbonnes(String keyword) throws SQLException {
        List<Abonne> abonnes = new ArrayList<>();
        String sql = "SELECT * FROM abonnes WHERE cin LIKE ? OR nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Abonne abonne = new Abonne();
                    abonne.setId(rs.getInt("id"));
                    abonne.setCin(rs.getString("cin"));
                    abonne.setNom(rs.getString("nom"));
                    abonne.setPrenom(rs.getString("prenom"));
                    abonne.setAdresse(rs.getString("adresse"));
                    abonne.setTelephone(rs.getString("telephone"));
                    
                    Date dateAbonnement = rs.getDate("date_abonnement");
                    if (dateAbonnement != null) {
                        abonne.setDateAbonnement(dateAbonnement.toLocalDate());
                    }
                    
                    abonnes.add(abonne);
                }
            }
        }
        return abonnes;
    }
}