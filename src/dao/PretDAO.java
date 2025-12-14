package dao;

import models.Pret;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import config.DatabaseConnection;

public class PretDAO {

    // INSERT Pret
    public static boolean ajouterPret(int abonneId, int documentId, String datePret, String dateRetourPrevu, String dateRetourEffective, String statut) {
        String sql = "INSERT INTO prets(abonne, document, datePret, dateRetourPrevu, dateRetourEffective, statut) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, abonneId);
            stmt.setInt(2, documentId);
            stmt.setString(3, datePret);
            stmt.setString(4, dateRetourPrevu);
            stmt.setString(5, dateRetourEffective);
            stmt.setString(6, statut);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET ALL: join to get abonne.cin and document.titre for display + keep ids
    public static List<Pret> getAllPrets() {
        List<Pret> list = new ArrayList<>();
        String sql = "SELECT p.id, p.abonne AS abonneId, p.document AS documentId, " +
                     "a.cin AS abonneCin, d.titre AS documentTitre, " +
                     "p.datePret, p.dateRetourPrevu, p.dateRetourEffective, p.statut " +
                     "FROM prets p " +
                     "LEFT JOIN abonnes a ON p.abonne = a.id " +
                     "LEFT JOIN documents d ON p.document = d.id " +
                     "ORDER BY p.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate datePret = rs.getDate("datePret") != null ? rs.getDate("datePret").toLocalDate() : null;
                LocalDate dateRetourPrevu = rs.getDate("dateRetourPrevu") != null ? rs.getDate("dateRetourPrevu").toLocalDate() : null;
                LocalDate dateRetourEffective = rs.getDate("dateRetourEffective") != null ? rs.getDate("dateRetourEffective").toLocalDate() : null;

                Pret p = new Pret(
                        rs.getInt("id"),
                        rs.getInt("abonneId"),
                        rs.getInt("documentId"),
                        rs.getString("abonneCin"),
                        rs.getString("documentTitre"),
                        datePret,
                        dateRetourPrevu,
                        dateRetourEffective,
                        rs.getString("statut")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //   // UPDATE Pret
    public static boolean modifierPret(Pret pret) {
        String sql = "UPDATE prets SET abonne = ?, document = ?, datePret = ?, dateRetourPrevu = ?, dateRetourEffective = ?, statut = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pret.getAbonneId());
            stmt.setInt(2, pret.getDocumentId());
            stmt.setString(3, pret.getDatePret() != null ? pret.getDatePret().toString() : null);
            stmt.setString(4, pret.getDateRetourPrevu() != null ? pret.getDateRetourPrevu().toString() : null);
            stmt.setString(5, pret.getDateRetourEffective() != null ? pret.getDateRetourEffective().toString() : null);
            stmt.setString(6, pret.getStatut());
            stmt.setInt(7, pret.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // SEARCH: search by abonne CIN or document TITLE 
    public static List<Pret> searchPrets(String keyword) {
        List<Pret> list = new ArrayList<>();
        String sql = "SELECT p.id, p.abonne AS abonneId, p.document AS documentId, " +
                     "a.cin AS abonneCin, d.titre AS documentTitre, " +
                     "p.datePret, p.dateRetourPrevu, p.dateRetourEffective, p.statut " +
                     "FROM prets p " +
                     "LEFT JOIN abonnes a ON p.abonne = a.id " +
                     "LEFT JOIN documents d ON p.document = d.id " +
                     "WHERE a.cin LIKE ? OR a.nom LIKE ? OR d.titre LIKE ? " +
                     "ORDER BY p.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate datePret = rs.getDate("datePret") != null ? rs.getDate("datePret").toLocalDate() : null;
                    LocalDate dateRetourPrevu = rs.getDate("dateRetourPrevu") != null ? rs.getDate("dateRetourPrevu").toLocalDate() : null;
                    LocalDate dateRetourEffective = rs.getDate("dateRetourEffective") != null ? rs.getDate("dateRetourEffective").toLocalDate() : null;

                    Pret p = new Pret(
                            rs.getInt("id"),
                            rs.getInt("abonneId"),
                            rs.getInt("documentId"),
                            rs.getString("abonneCin"),
                            rs.getString("documentTitre"),
                            datePret,
                            dateRetourPrevu,
                            dateRetourEffective,
                            rs.getString("statut")
                    );
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // DELETE PRET
    public boolean deletePret(int id) throws SQLException {
        String sql = "DELETE FROM prets WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
