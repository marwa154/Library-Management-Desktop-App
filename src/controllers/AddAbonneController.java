package controllers;

import dao.AbonneDAO;
import models.Abonne;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddAbonneController {
    
    public void showAddAbonne(Stage primaryStage) {
        primaryStage.setTitle("Ajouter un Abonné - SYGEBIB");
        

        HBox headerBox = createHeader("➕ NOUVEL ABONNÉ", 
            "Ajoutez un nouvel abonné au système");

        ScrollPane scrollPane = createForm(primaryStage);
        

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(scrollPane);
        mainLayout.setStyle("-fx-background-color: #f8fafc;");
        
        setupWindow(primaryStage, mainLayout);
    }
    
    private HBox createHeader(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        
        VBox textBox = new VBox(5, titleLabel, subtitleLabel);
        
        HBox headerBox = new HBox(textBox);
        headerBox.setPadding(new Insets(25, 30, 25, 30));
        headerBox.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        return headerBox;
    }
    
    private ScrollPane createForm(Stage primaryStage) {
      
        TextField cinField = createTextField("Entrez le numéro CIN");
        TextField nomField = createTextField("Entrez le nom");
        TextField prenomField = createTextField("Entrez le prénom");
        TextField adresseField = createTextField("Entrez l'adresse");
        TextField telephoneField = createTextField("Entrez le numéro de téléphone");
        
        DatePicker dateAbonnementPicker = createDatePicker();
        
        HBox buttonBox = createButtonBox(primaryStage, cinField, nomField, prenomField, 
                                       adresseField, telephoneField, dateAbonnementPicker);
        
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30, 40, 40, 40));
        formContainer.setStyle("-fx-background-color: white; -fx-border-radius: 12px; " +
                             "-fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        formContainer.setMaxWidth(600);
        formContainer.setAlignment(Pos.TOP_CENTER);
        
        formContainer.getChildren().addAll(
            createFormRow("🔑 Numéro CIN", "Identifiant unique de l'abonné", cinField),
            createFormRow("👤 Nom", "Nom de famille", nomField),
            createFormRow("👤 Prénom", "Prénom", prenomField),
            createFormRow("🏠 Adresse", "Adresse complète", adresseField),
            createFormRow("📞 Téléphone", "Numéro de téléphone", telephoneField),
            createFormRow("📅 Date d'abonnement", "Date d'inscription", dateAbonnementPicker),
            new Separator(),
            buttonBox
        );
        
        VBox mainContainer = new VBox(formContainer);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: #f8fafc;");
        
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-border-color: transparent; " +
                          "-fx-background-color: #f8fafc;");
        scrollPane.setPadding(new Insets(0));
        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        return scrollPane;
    }
    
    private TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-pref-width: 350px; " +
                      "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; " +
                      "-fx-background-color: #f8fafc;");
        
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-pref-width: 350px; " +
                             "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #3b82f6; " +
                             "-fx-background-color: white; -fx-border-width: 2px;");
            } else {
                field.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-pref-width: 350px; " +
                             "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; " +
                             "-fx-background-color: #f8fafc;");
            }
        });
        
        return field;
    }
    
    private DatePicker createDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setPromptText("Sélectionnez la date");
        datePicker.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; " +
                          "-fx-background-radius: 8px; -fx-border-radius: 8px;");
        
        return datePicker;
    }
    
    private VBox createFormRow(String label, String description, Control field) {
        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b; -fx-wrap-text: true;");
        
        VBox labelBox = new VBox(4, titleLabel, descLabel);
        labelBox.setPrefWidth(180);
        labelBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox fieldBox = new VBox(field);
        fieldBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox row = new HBox(25, labelBox, fieldBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));
        
        VBox container = new VBox(row);
        container.setPadding(new Insets(5, 0, 5, 0));
        
        return container;
    }
    
    private HBox createButtonBox(Stage primaryStage, TextField cinField, TextField nomField, 
                               TextField prenomField, TextField adresseField, TextField telephoneField, 
                               DatePicker datePicker) {
        Button addBtn = createActionButton("✅ Ajouter l'abonné", "#059669", primaryStage, "add", 
                                         cinField, nomField, prenomField, adresseField, telephoneField, datePicker);
        
        Button cancelBtn = createActionButton("🚪 Retour à la liste", "#64748b", primaryStage, "cancel", 
                                            cinField, nomField, prenomField, adresseField, telephoneField, datePicker);
        
        HBox buttonBox = new HBox(15, cancelBtn, addBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        return buttonBox;
    }
    
    private Button createActionButton(String text, String color, Stage primaryStage, String type,
                                    TextField cinField, TextField nomField, TextField prenomField,
                                    TextField adresseField, TextField telephoneField, DatePicker datePicker) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 12px 25px; -fx-background-radius: 8px; " +
                       "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand; " +
                       "-fx-font-weight: bold;");
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-size: 14px; -fx-padding: 12px 25px; -fx-background-radius: 8px; " +
            "-fx-background-color: " + darkenColor(color) + "; -fx-text-fill: white; -fx-cursor: hand; " +
            "-fx-font-weight: bold;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-size: 14px; -fx-padding: 12px 25px; -fx-background-radius: 8px; " +
            "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand; " +
            "-fx-font-weight: bold;"
        ));
        
        button.setOnAction(e -> {
            switch (type) {
                case "add":
                    if (validateForm(cinField, nomField, prenomField, adresseField, telephoneField, datePicker)) {
                        addAbonne(cinField.getText(), nomField.getText(), prenomField.getText(), 
                                 adresseField.getText(), telephoneField.getText(), datePicker.getValue(), primaryStage);
                    }
                    break;
                case "cancel":
                    new AbonneListController().showAbonneList(primaryStage);
                    break;
            }
        });
        
        return button;
    }
    
    private String darkenColor(String hexColor) {
        return hexColor.replaceFirst("#", "#80");
    }
    
    private boolean validateForm(TextField cin, TextField nom, TextField prenom, 
                               TextField adresse, TextField telephone, DatePicker date) {

        resetFieldStyle(cin);
        resetFieldStyle(nom);
        resetFieldStyle(prenom);
        resetFieldStyle(adresse);
        resetFieldStyle(telephone);
        
        boolean isValid = true;
        
        if (cin.getText().trim().isEmpty()) {
            showFieldError(cin, "Le numéro CIN est obligatoire");
            isValid = false;
        }
        if (nom.getText().trim().isEmpty()) {
            showFieldError(nom, "Le nom est obligatoire");
            isValid = false;
        }
        if (prenom.getText().trim().isEmpty()) {
            showFieldError(prenom, "Le prénom est obligatoire");
            isValid = false;
        }
        if (adresse.getText().trim().isEmpty()) {
            showFieldError(adresse, "L'adresse est obligatoire");
            isValid = false;
        }
        if (telephone.getText().trim().isEmpty()) {
            showFieldError(telephone, "Le téléphone est obligatoire");
            isValid = false;
        }
        if (date.getValue() == null) {
            showErrorAlert("Champ obligatoire", "La date d'abonnement est obligatoire");
            isValid = false;
        }
        
        return isValid;
    }
    
    private void resetFieldStyle(TextField field) {
        field.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-pref-width: 350px; " +
                      "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; " +
                      "-fx-background-color: #f8fafc;");
    }
    
    private void showFieldError(TextField field, String message) {
        field.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-pref-width: 350px; " +
                      "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #dc2626; " +
                      "-fx-background-color: #fef2f2; -fx-border-width: 2px;");
        field.requestFocus();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void addAbonne(String cin, String nom, String prenom, String adresse, 
                          String telephone, LocalDate dateAbonnement, Stage primaryStage) {
        try {
            Abonne abonne = new Abonne(cin, nom, prenom, adresse, telephone, dateAbonnement);
            AbonneDAO abonneDAO = new AbonneDAO();
            abonneDAO.addAbonne(abonne);
            
            showSuccessAlert("✅ Abonné ajouté avec succès", 
                "L'abonné " + nom + " " + prenom + " a été ajouté au système.", primaryStage);
            
        } catch (SQLException e) {
            showErrorAlert("Erreur d'ajout", "❌ Erreur lors de l'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showSuccessAlert(String title, String message, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        alert.showAndWait().ifPresent(response -> {

            new AbonneListController().showAbonneList(primaryStage);
        });
    }
    
    private void setupWindow(Stage primaryStage, BorderPane mainLayout) {
        Scene scene = new Scene(mainLayout, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}