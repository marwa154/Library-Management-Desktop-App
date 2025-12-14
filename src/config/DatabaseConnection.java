package config;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {
	 private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque";
	    private static final String USER = "root";
	    private static final String PASSWORD = "";

	    public static Connection getConnection() {
	        try {
	            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            System.out.println("Connexion MySQL OK !");
	            return conn;

	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

}
