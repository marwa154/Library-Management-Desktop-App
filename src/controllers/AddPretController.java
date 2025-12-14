package controllers;

import dao.AbonneDAO;
import dao.DocumentDAO;
import dao.PretDAO;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Abonne;
import models.Document;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddPretController {

    
    private AutoCompleteTextField<Abonne> abonneField;
    private AutoCompleteTextField<Document> documentField;
    private ComboBox<String> statutCombo;

    public void showAddPret(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Ajouter un Prêt - SYGEBIB");

       
        HBox headerBox = createHeader("➕ NOUVEAU PRÊT",
                "Ajoutez un nouveau prêt au système");

      
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

    private ScrollPane createForm(Stage primaryStage) throws SQLException {
       
        List<Abonne> abonnes = new AbonneDAO().getAllAbonnes();
        abonneField = new AutoCompleteTextField<>();
        abonneField.setItems(FXCollections.observableArrayList(abonnes));
        abonneField.setPromptText("Rechercher un abonné par CIN, nom, prénom...");
        abonneField.setPrefWidth(350);
        
       
        abonneField.setCellFactory(listView -> new ListCell<Abonne>() {
            @Override
            protected void updateItem(Abonne abonne, boolean empty) {
                super.updateItem(abonne, empty);
                if (empty || abonne == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    
                    String cin = abonne.getCin() != null ? abonne.getCin() : "";
                    String nom = abonne.getNom() != null ? abonne.getNom() : "";
                    String prenom = abonne.getPrenom() != null ? abonne.getPrenom() : "";
                    
                 
                    if (!cin.isEmpty() && !nom.isEmpty() && !prenom.isEmpty()) {
                        setText(String.format("%s - %s %s", cin, nom, prenom));
                    } else if (!cin.isEmpty()) {
                        setText(cin);
                    } else if (!nom.isEmpty() || !prenom.isEmpty()) {
                        setText(nom + " " + prenom);
                    } else {
                        setText("Abonné sans informations");
                    }
                }
            }
        });
        
      
        abonneField.setConverter(new AutoCompleteTextField.Converter<Abonne>() {
            @Override
            public String toString(Abonne abonne) {
                if (abonne == null) return "";
                
                String cin = abonne.getCin() != null ? abonne.getCin() : "";
                String nom = abonne.getNom() != null ? abonne.getNom() : "";
                String prenom = abonne.getPrenom() != null ? abonne.getPrenom() : "";
                
                if (!cin.isEmpty() && !nom.isEmpty() && !prenom.isEmpty()) {
                    return String.format("%s - %s %s", cin, nom, prenom);
                } else if (!cin.isEmpty()) {
                    return cin;
                } else if (!nom.isEmpty() || !prenom.isEmpty()) {
                    return nom + " " + prenom;
                } else {
                    return "Abonné sans informations";
                }
            }

            @Override
            public boolean matches(Abonne abonne, String searchText) {
                if (searchText == null || searchText.isEmpty()) return true;
                if (abonne == null) return false;
                
                String searchLower = searchText.toLowerCase();
                
                String cin = abonne.getCin() != null ? abonne.getCin().toLowerCase() : "";
                String nom = abonne.getNom() != null ? abonne.getNom().toLowerCase() : "";
                String prenom = abonne.getPrenom() != null ? abonne.getPrenom().toLowerCase() : "";
                
                return cin.contains(searchLower) ||
                       nom.contains(searchLower) ||
                       prenom.contains(searchLower);
            }
        });
        
        abonneField.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; " +
                           "-fx-background-radius: 8px; -fx-border-radius: 8px;");

        List<Document> documents = new DocumentDAO().getAllDocuments();
        documentField = new AutoCompleteTextField<>();
        documentField.setItems(FXCollections.observableArrayList(documents));
        documentField.setPromptText("Rechercher un document par titre, auteur, ISBN...");
        documentField.setPrefWidth(350);
        
     
        documentField.setCellFactory(listView -> new ListCell<Document>() {
            @Override
            protected void updateItem(Document doc, boolean empty) {
                super.updateItem(doc, empty);
                if (empty || doc == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                   
                    String titre = doc.getTitre() != null ? doc.getTitre() : "";
                    String auteur = doc.getAuteur() != null ? doc.getAuteur() : "";
                    String isbn = doc.getIsbn() != null ? doc.getIsbn() : "";
                    
                    if (!titre.isEmpty() && !auteur.isEmpty() && !isbn.isEmpty()) {
                        setText(String.format("%s - %s (%s)", titre, auteur, isbn));
                    } else if (!titre.isEmpty() && !auteur.isEmpty()) {
                        setText(String.format("%s - %s", titre, auteur));
                    } else if (!titre.isEmpty()) {
                        setText(titre);
                    } else {
                        setText("Document sans informations");
                    }
                }
            }
        });
        

        documentField.setConverter(new AutoCompleteTextField.Converter<Document>() {
            @Override
            public String toString(Document doc) {
                if (doc == null) return "";
                
                String titre = doc.getTitre() != null ? doc.getTitre() : "";
                String auteur = doc.getAuteur() != null ? doc.getAuteur() : "";
                String isbn = doc.getIsbn() != null ? doc.getIsbn() : "";
                
                if (!titre.isEmpty() && !auteur.isEmpty() && !isbn.isEmpty()) {
                    return String.format("%s - %s (%s)", titre, auteur, isbn);
                } else if (!titre.isEmpty() && !auteur.isEmpty()) {
                    return String.format("%s - %s", titre, auteur);
                } else if (!titre.isEmpty()) {
                    return titre;
                } else {
                    return "Document sans informations";
                }
            }

            @Override
            public boolean matches(Document doc, String searchText) {
                if (searchText == null || searchText.isEmpty()) return true;
                if (doc == null) return false;
                
                String searchLower = searchText.toLowerCase();
                
                String titre = doc.getTitre() != null ? doc.getTitre().toLowerCase() : "";
                String auteur = doc.getAuteur() != null ? doc.getAuteur().toLowerCase() : "";
                String isbn = doc.getIsbn() != null ? doc.getIsbn().toLowerCase() : "";
                
                return titre.contains(searchLower) ||
                       auteur.contains(searchLower) ||
                       isbn.contains(searchLower);
            }
        });
        
        documentField.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; " +
                            "-fx-background-radius: 8px; -fx-border-radius: 8px;");

    
        DatePicker datePretPicker = createDatePicker();
        DatePicker dateRetourPrevPicker = createDatePicker();
        DatePicker dateRetourEffPicker = createDatePicker();

     
        statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList("En cours", "Retourné", "En retard"));
        statutCombo.setPromptText("Sélectionnez le statut");
        statutCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; " +
                          "-fx-background-radius: 8px; -fx-border-radius: 8px;");

        
        HBox buttonBox = createButtonBox(primaryStage, datePretPicker, dateRetourPrevPicker, dateRetourEffPicker);

    
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30, 40, 40, 40));
        formContainer.setStyle("-fx-background-color: white; -fx-border-radius: 12px; " +
                "-fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        formContainer.setMaxWidth(600);
        formContainer.setAlignment(Pos.TOP_CENTER);

        formContainer.getChildren().addAll(
                createFormRow("👤 Abonné", "Recherchez un abonné par CIN, nom ou prénom", abonneField),
                createFormRow("📄 Document", "Recherchez un document par titre, auteur ou ISBN", documentField),
                createFormRow("📅 Date de prêt", "Date à laquelle le prêt est effectué", datePretPicker),
                createFormRow("📅 Date de retour prévu", "Date prévue pour le retour", dateRetourPrevPicker),
                createFormRow("📅 Date de retour effective", "Date réelle de retour", dateRetourEffPicker),
                createFormRow("📌 Statut", "Statut du prêt", statutCombo),
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
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-border-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
    
        return scrollPane;
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

    private HBox createButtonBox(Stage primaryStage, DatePicker datePretPicker,
                                 DatePicker dateRetourPrevPicker, DatePicker dateRetourEffPicker) {

        Button addBtn = new Button("✅ Ajouter le prêt");
        addBtn.setStyle("-fx-font-size: 14px; -fx-padding: 12px 25px; -fx-background-radius: 8px; " +
                "-fx-background-color: #059669; -fx-text-fill: white; -fx-font-weight: bold;");
        addBtn.setOnAction(e -> {
            if (validateForm(datePretPicker, dateRetourPrevPicker)) {
                addPret(abonneField.getSelectedItem(), documentField.getSelectedItem(),
                        datePretPicker.getValue(), dateRetourPrevPicker.getValue(),
                        dateRetourEffPicker.getValue(), statutCombo.getValue(), primaryStage);
            }
        });

        Button cancelBtn = new Button("🚪 Retour à la liste");
        cancelBtn.setStyle("-fx-font-size: 14px; -fx-padding: 12px 25px; -fx-background-radius: 8px; " +
                "-fx-background-color: #64748b; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelBtn.setOnAction(e -> new PretListController().showPretList(primaryStage));

        HBox buttonBox = new HBox(15, cancelBtn, addBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        return buttonBox;
    }

    private boolean validateForm(DatePicker datePretPicker, DatePicker dateRetourPrevPicker) {
        if (abonneField.getSelectedItem() == null) {
            showErrorAlert("Champ obligatoire", "Veuillez sélectionner un abonné");
            return false;
        }
        if (documentField.getSelectedItem() == null) {
            showErrorAlert("Champ obligatoire", "Veuillez sélectionner un document");
            return false;
        }
        if (datePretPicker.getValue() == null) {
            showErrorAlert("Champ obligatoire", "Veuillez sélectionner la date de prêt");
            return false;
        }
        if (dateRetourPrevPicker.getValue() == null) {
            showErrorAlert("Champ obligatoire", "Veuillez sélectionner la date de retour prévue");
            return false;
        }
        if (statutCombo.getValue() == null) {
            showErrorAlert("Champ obligatoire", "Veuillez sélectionner le statut du prêt");
            return false;
        }
        return true;
    }

    private void addPret(Abonne abonne, Document document, LocalDate datePret,
                         LocalDate dateRetourPrev, LocalDate dateRetourEff,
                         String statut, Stage primaryStage) {

        boolean success = PretDAO.ajouterPret(
                abonne.getId(),
                document.getId(),
                datePret.toString(),
                dateRetourPrev.toString(),
                (dateRetourEff != null ? dateRetourEff.toString() : ""),
                statut
        );

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("✅ Prêt ajouté avec succès");
            alert.setContentText("Le prêt pour l'abonné " + abonne.getNom() + " a été ajouté au système.");
            alert.showAndWait();

            new PretListController().showPretList(primaryStage);
        } else {
            showErrorAlert("Erreur d'ajout", "❌ Erreur lors de l'ajout du prêt");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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