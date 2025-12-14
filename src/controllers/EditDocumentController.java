package controllers;

import dao.DocumentDAO;
import models.Document;
import models.Exemplaire;
import models.Exemplaire.Statut;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditDocumentController {
    
    private Document document;
    private DocumentDAO documentDAO;
    private ToggleGroup typeGroup;
    private ToggleGroup anneeGroup;
    private TextField titreField;
    private TextField auteurField;
    private TextField coteField;
    private ComboBox<String> themeCombo;
    private DatePicker dateParutionPicker;
    private TextField isbnField;
    private TextField editeurField;
    private VBox exemplairesContent;
    private List<Exemplaire> exemplaires;
    
    public void showEditDocument(Stage primaryStage, Document doc) {
        this.document = doc;
        primaryStage.setTitle("Modifier un Document - SYGEBIB");
        documentDAO = new DocumentDAO();
        exemplaires = new ArrayList<>();
        
        try {
            exemplaires = documentDAO.getExemplairesByDocument(document.getId());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des exemplaires: " + e.getMessage());
        }
        
        HBox headerBox = createHeader("✏️ MODIFIER LE DOCUMENT", 
            "Modifiez les informations de \"" + document.getTitre() + "\"");
        
        ScrollPane scrollPane = createForm(primaryStage);
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(scrollPane);
        mainLayout.setStyle("-fx-background-color: #f8fafc;");
        
        populateForm();
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
        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: #f8fafc;");
        
        VBox generalSection = createGeneralSection();
        
        VBox specificSection = createSpecificSection();
        
        VBox exemplairesSection = createExemplairesSection();
        
        HBox buttonsBox = createButtonsBox(primaryStage);
        
        mainContainer.getChildren().addAll(
            generalSection,
            specificSection,
            exemplairesSection,
            buttonsBox
        );
        
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-border-color: transparent;");
        scrollPane.setPadding(new Insets(0));
        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        return scrollPane;
    }
    
    private VBox createGeneralSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        
        Label titleLabel = new Label("📋 INFORMATIONS GÉNÉRALES");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 0, 0));
        
        Label typeLabel = createFormLabel("📄 Type de document");
        typeGroup = new ToggleGroup();
        HBox typeBox = createRadioButtonGroup(typeGroup, "Ouvrage", "Memoire");
        grid.add(typeLabel, 0, 0);
        grid.add(typeBox, 1, 0);
        
        Label anneeLabel = createFormLabel("🎓 Niveau d'étude");
        anneeGroup = new ToggleGroup();
        HBox anneeBox = createRadioButtonGroup(anneeGroup, "Licence", "Master", "Doctorat");
        grid.add(anneeLabel, 0, 1);
        grid.add(anneeBox, 1, 1);
        
        Label titreLabel = createFormLabel("📖 Titre");
        titreField = createTextField("Entrez le titre du document");
        grid.add(titreLabel, 0, 2);
        grid.add(titreField, 1, 2);
        
        Label auteurLabel = createFormLabel("✍️ Auteur(s)");
        auteurField = createTextField("Entrez le(s) nom(s) de l'auteur");
        grid.add(auteurLabel, 0, 3);
        grid.add(auteurField, 1, 3);
        
        Label coteLabel = createFormLabel("🔖 Cote");
        coteField = createTextField("U--- (format de cote)");
        grid.add(coteLabel, 0, 4);
        grid.add(coteField, 1, 4);
        
        Label themeLabel = createFormLabel("🏷️ Thème");
        themeCombo = createComboBox();
        grid.add(themeLabel, 0, 5);
        grid.add(themeCombo, 1, 5);
        
        Label dateLabel = createFormLabel("📅 Date de parution");
        dateParutionPicker = createDatePicker();
        grid.add(dateLabel, 0, 6);
        grid.add(dateParutionPicker, 1, 6);
        
        section.getChildren().addAll(titleLabel, grid);
        return section;
    }
    
    private VBox createSpecificSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        
        Label titleLabel = new Label("🔍 SI LE DOCUMENT EST UN MEMOIRE ");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 0, 0));
        
        // ISBN
        Label isbnLabel = createFormLabel("📚 ISBN");
        isbnField = createTextField("Entrez le numéro ISBN");
        grid.add(isbnLabel, 0, 0);
        grid.add(isbnField, 1, 0);
        
        Label editeurLabel = createFormLabel("🏢 Éditeur");
        editeurField = createTextField("Entrez le nom de l'éditeur");
        grid.add(editeurLabel, 0, 1);
        grid.add(editeurField, 1, 1);
        
        section.getChildren().addAll(titleLabel, grid);
        return section;
    }
    
    private VBox createExemplairesSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        
        Label titleLabel = new Label("📦 GESTION DES EXEMPLAIRES");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        HBox header = createExemplairesHeader();
        
        exemplairesContent = new VBox(8);
        exemplairesContent.setPadding(new Insets(10));
        exemplairesContent.setStyle("-fx-border-color: #e2e8f0; -fx-border-radius: 8px; " +
                                  "-fx-background-radius: 8px; -fx-background-color: #f8fafc;");
        
        ScrollPane exemplairesScroll = new ScrollPane(exemplairesContent);
        exemplairesScroll.setFitToWidth(true);
        exemplairesScroll.setPrefHeight(300); 
        exemplairesScroll.setMinHeight(200);  
        exemplairesScroll.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        exemplairesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        exemplairesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        Button addBtn = createActionButton("➕ Ajouter un exemplaire", "#3b82f6");
        addBtn.setOnAction(e -> addExemplaire());
        
        section.getChildren().addAll(titleLabel, header, exemplairesScroll, addBtn);
        return section;
    }
    
    private HBox createExemplairesHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #f1f5f9; -fx-border-radius: 6px;");
        
        String headerStyle = "-fx-font-weight: bold; -fx-text-fill: #475569; -fx-font-size: 12px;";
        
        Label libelleHeader = new Label("LIBELLÉ");
        libelleHeader.setStyle(headerStyle);
        libelleHeader.setPrefWidth(120);
        
        Label localisationHeader = new Label("LOCALISATION");
        localisationHeader.setStyle(headerStyle);
        localisationHeader.setPrefWidth(120);
        
        Label dateHeader = new Label("DATE ACQUISITION");
        dateHeader.setStyle(headerStyle);
        dateHeader.setPrefWidth(130);
        
        Label statutHeader = new Label("STATUT");
        statutHeader.setStyle(headerStyle);
        statutHeader.setPrefWidth(100);
        
        Label actionHeader = new Label("ACTION");
        actionHeader.setStyle(headerStyle);
        actionHeader.setPrefWidth(80);
        
        header.getChildren().addAll(libelleHeader, localisationHeader, dateHeader, statutHeader, actionHeader);
        return header;
    }
    
    private void addExemplaire() {
        HBox exemplaireRow = new HBox(15);
        exemplaireRow.setPadding(new Insets(8));
        exemplaireRow.setStyle("-fx-background-color: white; -fx-border-radius: 6px; " +
                             "-fx-border-color: #e2e8f0; -fx-border-width: 1px;");
        exemplaireRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField libelleField = createSmallTextField("Libellé");
        libelleField.setPrefWidth(120);
        
        TextField localisationField = createSmallTextField("Localisation");
        localisationField.setPrefWidth(120);
        
        DatePicker datePicker = createSmallDatePicker();
        datePicker.setPrefWidth(130);
        datePicker.setValue(LocalDate.now());
        
        ComboBox<String> statutCombo = createSmallComboBox();
        statutCombo.setPrefWidth(100);
        
        Button removeBtn = createIconButton("🗑", "#ef4444");
        removeBtn.setPrefWidth(40);
        
        exemplaireRow.getChildren().addAll(libelleField, localisationField, datePicker, statutCombo, removeBtn);
        exemplairesContent.getChildren().add(exemplaireRow);
        
        Exemplaire exemplaire = new Exemplaire(0, "", "", LocalDate.now(), Exemplaire.Statut.DISPONIBLE);
        exemplaires.add(exemplaire);
        
        // Listeners pour mettre à jour l'objet
        libelleField.textProperty().addListener((obs, oldVal, newVal) -> exemplaire.setLibelle(newVal));
        localisationField.textProperty().addListener((obs, oldVal, newVal) -> exemplaire.setLocalisation(newVal));
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) exemplaire.setDateAcquisition(newVal);
        });
        statutCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) exemplaire.setStatut(Exemplaire.Statut.valueOf(newVal));
        });
        
        removeBtn.setOnAction(e -> {
            exemplairesContent.getChildren().remove(exemplaireRow);
            exemplaires.remove(exemplaire);
        });
    }
    
    private HBox createButtonsBox(Stage primaryStage) {
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button cancelBtn = createActionButton("🚪 Annuler", "#64748b", primaryStage, "cancel");
        Button saveBtn = createActionButton("💾 Enregistrer les modifications", "#f59e0b", primaryStage, "save");
        
        buttonsBox.getChildren().addAll(cancelBtn, saveBtn);
        return buttonsBox;
    }
    
    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        label.setPrefWidth(150);
        return label;
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-font-size: 14px; -fx-padding: 10px 12px; -fx-pref-width: 300px; " +
                      "-fx-background-radius: 6px; -fx-border-radius: 6px; -fx-border-color: #cbd5e1;");
        return field;
    }
    
    private TextField createSmallTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-font-size: 12px; -fx-padding: 6px 8px; " +
                      "-fx-background-radius: 4px; -fx-border-radius: 4px; -fx-border-color: #cbd5e1;");
        return field;
    }
    
    private DatePicker createDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px;");
        return picker;
    }
    
    private DatePicker createSmallDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setStyle("-fx-font-size: 12px;");
        return picker;
    }
    
    private ComboBox<String> createComboBox() {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Informatique", "Mathématiques", "Physique", "Chimie", "Biologie", "Littérature");
        combo.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px;");
        return combo;
    }
    
    private ComboBox<String> createSmallComboBox() {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("DISPONIBLE", "EMPRUNTE");
        combo.setValue("DISPONIBLE");
        combo.setStyle("-fx-font-size: 12px;");
        return combo;
    }
    
    private HBox createRadioButtonGroup(ToggleGroup group, String... options) {
        HBox box = new HBox(15);
        for (String option : options) {
            RadioButton radio = new RadioButton(option);
            radio.setToggleGroup(group);
            radio.setStyle("-fx-font-size: 14px;");
            box.getChildren().add(radio);
        }
        return box;
    }
    
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 6px; " +
                       "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand;");
        return button;
    }
    
    private Button createActionButton(String text, String color, Stage primaryStage, String type) {
        Button button = createActionButton(text, color);
        
        button.setOnAction(e -> {
            switch (type) {
                case "save":
                    if (validateForm()) {
                        updateDocument(primaryStage);
                    }
                    break;
                case "cancel":
                    new DocumentListController().showDocumentList(primaryStage);
                    break;
            }
        });
        
        return button;
    }
    
    private Button createIconButton(String icon, String color) {
        Button button = new Button(icon);
        button.setStyle("-fx-font-size: 12px; -fx-padding: 6px; -fx-background-radius: 4px; " +
                       "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand;");
        return button;
    }
    
    private void populateForm() {
        if (document != null) {
            titreField.setText(document.getTitre());
            auteurField.setText(document.getAuteur());
            coteField.setText(document.getCote());
            themeCombo.setValue(document.getTheme());
            dateParutionPicker.setValue(document.getDateParution());
            isbnField.setText(document.getIsbn() != null ? document.getIsbn() : "");
            editeurField.setText(document.getEditeur() != null ? document.getEditeur() : "");
            
            // Type
            String typeDocument = document.getTypeDocument().name();
            for (javafx.scene.control.RadioButton radio : getRadioButtonsFromToggleGroup(typeGroup)) {
                if (radio.getText().equalsIgnoreCase(typeDocument)) {
                    radio.setSelected(true);
                    break;
                }
            }
            
 
            if (document.getAnneeEtude() != null) {
                String anneeEtude = document.getAnneeEtude().name();
                for (javafx.scene.control.RadioButton radio : getRadioButtonsFromToggleGroup(anneeGroup)) {
                    if (radio.getText().equalsIgnoreCase(anneeEtude)) {
                        radio.setSelected(true);
                        break;
                    }
                }
            }
            
            loadExistingExemplaires();
        }
    }
    
    private List<RadioButton> getRadioButtonsFromToggleGroup(ToggleGroup group) {
        List<RadioButton> radios = new ArrayList<>();
        for (Toggle toggle : group.getToggles()) {
            if (toggle instanceof RadioButton) {
                radios.add((RadioButton) toggle);
            }
        }
        return radios;
    }
    
    private void loadExistingExemplaires() {
        exemplairesContent.getChildren().clear();
        for (Exemplaire exemplaire : exemplaires) {
            addExemplaireRow(exemplaire);
        }
    }
    
    private void addExemplaireRow(Exemplaire exemplaire) {
        HBox exemplaireRow = new HBox(15);
        exemplaireRow.setPadding(new Insets(8));
        exemplaireRow.setStyle("-fx-background-color: white; -fx-border-radius: 6px; " +
                             "-fx-border-color: #e2e8f0; -fx-border-width: 1px;");
        exemplaireRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField libelleField = createSmallTextField("Libellé");
        libelleField.setText(exemplaire.getLibelle());
        libelleField.setPrefWidth(120);
        
        TextField localisationField = createSmallTextField("Localisation");
        localisationField.setText(exemplaire.getLocalisation());
        localisationField.setPrefWidth(120);
        
        DatePicker datePicker = createSmallDatePicker();
        datePicker.setValue(exemplaire.getDateAcquisition());
        datePicker.setPrefWidth(130);
        
        ComboBox<String> statutCombo = createSmallComboBox();
        statutCombo.setValue(exemplaire.getStatut().name());
        statutCombo.setPrefWidth(100);
        
        Button removeBtn = createIconButton("🗑", "#ef4444");
        removeBtn.setPrefWidth(40);
        
        exemplaireRow.getChildren().addAll(libelleField, localisationField, datePicker, statutCombo, removeBtn);
        exemplairesContent.getChildren().add(exemplaireRow);
        
        // Listeners pour mettre à jour l'objet
        libelleField.textProperty().addListener((obs, oldVal, newVal) -> exemplaire.setLibelle(newVal));
        localisationField.textProperty().addListener((obs, oldVal, newVal) -> exemplaire.setLocalisation(newVal));
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) exemplaire.setDateAcquisition(newVal);
        });
        statutCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) exemplaire.setStatut(Exemplaire.Statut.valueOf(newVal));
        });
        
        removeBtn.setOnAction(e -> {
            exemplairesContent.getChildren().remove(exemplaireRow);
            exemplaires.remove(exemplaire);
        });
    }
    
    private boolean validateForm() {
        if (titreField.getText().isEmpty() || auteurField.getText().isEmpty() || 
            coteField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
            return false;
        }
        
        for (Exemplaire ex : exemplaires) {
            if (ex.getLibelle().isEmpty() || ex.getLocalisation().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs des exemplaires");
                return false;
            }
        }
        
        return true;
    }
    
    private void updateDocument(Stage primaryStage) {
        try {
            document.setCote(coteField.getText());
            document.setTitre(titreField.getText());
            document.setAuteur(auteurField.getText());
            document.setTheme(themeCombo.getValue());
            
            RadioButton selectedType = (RadioButton) typeGroup.getSelectedToggle();
            document.setTypeDocument(Document.TypeDocument.valueOf(selectedType.getText().toUpperCase()));
            
            RadioButton selectedAnnee = (RadioButton) anneeGroup.getSelectedToggle();
            if (selectedAnnee != null) {
                document.setAnneeEtude(Document.AnneeEtude.valueOf(selectedAnnee.getText().toUpperCase()));
            } else {
                document.setAnneeEtude(null);
            }
            
            document.setDateParution(dateParutionPicker.getValue());
            document.setIsbn(isbnField.getText());
            document.setEditeur(editeurField.getText());
            
            documentDAO.updateDocument(document);
            
            // Gestion des exemplaires
            List<Exemplaire> existingExemplaires = documentDAO.getExemplairesByDocument(document.getId());
            for (Exemplaire existing : existingExemplaires) {
                documentDAO.deleteExemplaire(existing.getId());
            }
            
            for (Exemplaire exemplaire : exemplaires) {
                exemplaire.setDocumentId(document.getId());
                documentDAO.addExemplaire(exemplaire);
            }
            
            showSuccessAlert("Document modifié avec succès", primaryStage);
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification du document: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showSuccessAlert(String message, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("✅ " + message);
        alert.setContentText("Les modifications ont été enregistrées avec succès.");
        
        alert.showAndWait().ifPresent(response -> {
            new DocumentListController().showDocumentList(primaryStage);
        });
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void setupWindow(Stage primaryStage, BorderPane mainLayout) {
        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}