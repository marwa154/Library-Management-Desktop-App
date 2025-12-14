package controllers;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenuController extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        showMainMenu(primaryStage);
    }
    
    public void showMainMenu(Stage primaryStage) {
        primaryStage.setTitle("SYGEBIB - Menu Principal");
        
        Label titleLabel = new Label("SYGEBIB");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Système de Gestion de Bibliothèque");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e0e0e0;");
        
        VBox headerBox = new VBox(5, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(30, 0, 30, 0));
        headerBox.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db);");
        
        VBox docCard = createFeatureCard("📚", "Gestion des Documents", 
            "Gérer le catalogue des documents, exemplaires et disponibilités", 
            "#e74c3c", gestionDocBtn(primaryStage));
        
        VBox abonneCard = createFeatureCard("👥", "Gestion des Abonnés", 
            "Gérer les abonnés, inscriptions et informations personnelles", 
            "#2ecc71", gestionAbonneBtn(primaryStage));
        
        VBox empruntCard = createFeatureCard("📖", "Gestion des Emprunts", 
            "Gérer les emprunts, retours et réservations", 
            "#f39c12",  gestionPretBtn(primaryStage));
        VBox searchCard = createFeatureCard(
        	    "🔎", 
        	    "Recherche Documents", 
        	    "Rechercher des documents par titre, auteur ou type", 
        	    "#3498db", 
        	    gestionPublicSearchBtn(primaryStage)
        	);


        GridPane cardsGrid = new GridPane();
        cardsGrid.setAlignment(Pos.CENTER);
        cardsGrid.setHgap(30);
        cardsGrid.setVgap(30);
        cardsGrid.setPadding(new Insets(40));
        
        cardsGrid.add(docCard, 0, 0);
        cardsGrid.add(abonneCard, 1, 0);
        cardsGrid.add(empruntCard, 2, 0);
        cardsGrid.add(searchCard, 3, 0);
        // Container principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(cardsGrid);
        
        primaryStage.setMaximized(true);
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createFeatureCard(String emoji, String title, String description, String color, Button button) {
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 48px; -fx-padding: 10px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-wrap-text: true;");
        descLabel.setMaxWidth(200);
        
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; " +
                       "-fx-padding: 10px 20px; -fx-background-radius: 5px;");
        button.setMaxWidth(Double.MAX_VALUE);
        
        VBox card = new VBox(15, emojiLabel, titleLabel, descLabel, button);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30, 20, 30, 20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10px; " +
                     "-fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setMinWidth(280);
        card.setMinHeight(300);
        
        return card;
    }
    
    private Button gestionDocBtn(Stage primaryStage) {
        Button btn = new Button("Accéder");
        btn.setOnAction(e -> {
            DocumentListController docController = new DocumentListController();
            docController.showDocumentList(primaryStage);
        });
        return btn;
    }
    
    private Button gestionAbonneBtn(Stage primaryStage) {
        Button btn = new Button("Accéder");
        btn.setOnAction(e -> {
            AbonneListController abonneController = new AbonneListController();
            abonneController.showAbonneList(primaryStage);
        });
        return btn;
    }
    
    private Button gestionPublicSearchBtn(Stage primaryStage) {
        Button btn = new Button("Accéder");
        btn.setOnAction(e -> {
            publicSearchController searchController = new publicSearchController();
            searchController.showDocumentSearch(primaryStage); // méthode que tu dois créer pour afficher la table
        });
        return btn;
    }

    private Button gestionPretBtn(Stage primaryStage) {
        Button btn = new Button("Accéder");
        btn.setOnAction(e -> {
        	PretListController pretController = new PretListController();
        	pretController.showPretList(primaryStage);
        });
        return btn;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}