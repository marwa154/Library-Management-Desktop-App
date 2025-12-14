package controllers;

import dao.AbonneDAO;
import dao.DocumentDAO;
import dao.PretDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Abonne;
import models.Document;
import models.Pret;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EditPretController {

    private ComboBox<Abonne> abonneCombo;
    private ComboBox<Document> documentCombo;
    private ComboBox<String> statutCombo;

    public void showEditPret(Stage primaryStage, Pret pret) throws SQLException {
        primaryStage.setTitle("Modifier un Prêt - SYGEBIB");

      
        HBox headerBox = createHeader("✏️ MODIFIER LE PRÊT", "Modifiez les informations du prêt");

      
        ScrollPane scrollPane = createForm(primaryStage, pret);

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

    private ScrollPane createForm(Stage primaryStage, Pret pret) throws SQLException {

        // --- Abonné ---
        List<Abonne> abonnes = new AbonneDAO().getAllAbonnes();
        FilteredList<Abonne> filteredAbonnes = new FilteredList<>(FXCollections.observableArrayList(abonnes), p -> true);

        abonneCombo = new ComboBox<>(filteredAbonnes);
        abonneCombo.setPromptText("Sélectionnez l'abonné");
        abonneCombo.setEditable(false);
        abonneCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; -fx-background-radius: 8px; -fx-border-radius: 8px;");

        abonneCombo.setDisable(true); //  disabled

        Abonne selectedAbonne = abonnes.stream()
                .filter(a -> a.getId() == pret.getAbonneId())
                .findFirst()
                .orElse(null);
        abonneCombo.setValue(selectedAbonne);

        abonneCombo.setConverter(new javafx.util.StringConverter<Abonne>() {
            @Override
            public String toString(Abonne abonne) {
                return abonne != null ? abonne.getCin() : "";
            }
            @Override
            public Abonne fromString(String string) {
                return filteredAbonnes.stream()
                        .filter(a -> a.getCin().equals(string))
                        .findFirst().orElse(null);
            }
        });

        // --- Document ---
        List<Document> documents = new DocumentDAO().getAllDocuments();
        FilteredList<Document> filteredDocs = new FilteredList<>(FXCollections.observableArrayList(documents), p -> true);

        documentCombo = new ComboBox<>(filteredDocs);
        documentCombo.setPromptText("Sélectionnez le document");
        documentCombo.setEditable(false);
        documentCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; -fx-background-radius: 8px; -fx-border-radius: 8px;");

        documentCombo.setDisable(true); // //  disabled

        Document selectedDocument = documents.stream()
                .filter(d -> d.getId() == pret.getDocumentId())
                .findFirst()
                .orElse(null);
        documentCombo.setValue(selectedDocument);

        documentCombo.setConverter(new javafx.util.StringConverter<Document>() {
            @Override
            public String toString(Document doc) {
                return doc != null ? doc.getTitre() : "";
            }
            @Override
            public Document fromString(String string) {
                return filteredDocs.stream()
                        .filter(d -> d.getTitre().equals(string))
                        .findFirst().orElse(null);
            }
        });

        // --- DatePickers ---
        DatePicker datePretPicker = createDatePicker();
        datePretPicker.setValue(pret.getDatePret());
        DatePicker dateRetourPrevPicker = createDatePicker();
        dateRetourPrevPicker.setValue(pret.getDateRetourPrevu());
        DatePicker dateRetourEffPicker = createDatePicker();
        dateRetourEffPicker.setValue(pret.getDateRetourEffective());

        // --- Statut ---
        statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList("En cours", "Terminé", "En retard"));
        statutCombo.setValue(pret.getStatut());

        // --- Boutons ---
        HBox buttonBox = createButtonBox(primaryStage, pret, datePretPicker, dateRetourPrevPicker, dateRetourEffPicker);

        // --- Form  ---
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30, 40, 40, 40));
        formContainer.setStyle("-fx-background-color: white; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-border-color: #e2e8f0;");
        formContainer.setMaxWidth(600);
        formContainer.setAlignment(Pos.TOP_CENTER);

        formContainer.getChildren().addAll(
                createFormRow("👤 Abonné", "Nom de l'abonné", abonneCombo),
                createFormRow("📄 Document", "Titre du document", documentCombo),
                createFormRow("📅 Date de prêt", "Date de départ du prêt", datePretPicker),
                createFormRow("📅 Date retour prévu", "Date prévue pour le retour", dateRetourPrevPicker),
                createFormRow("📅 Date retour effective", "Date réelle du retour", dateRetourEffPicker),
                createFormRow("📌 Statut", "État du prêt", statutCombo),
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
        datePicker.setPromptText("Sélectionnez la date");
        datePicker.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        return datePicker;
    }

    private VBox createFormRow(String label, String description, Control field) {
        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        VBox labelBox = new VBox(4, titleLabel, descLabel);
        labelBox.setPrefWidth(180);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        VBox fieldBox = new VBox(field);
        fieldBox.setAlignment(Pos.CENTER_LEFT);

        HBox row = new HBox(25, labelBox, fieldBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));

        return new VBox(row);
    }

    private HBox createButtonBox(Stage primaryStage, Pret pret,
                                 DatePicker datePretPicker, DatePicker dateRetourPrevPicker, DatePicker dateRetourEffPicker) {

        Button updateBtn = new Button("💾 Enregistrer");
        updateBtn.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px; -fx-background-radius: 8px; " +
                "-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold;");

        updateBtn.setOnAction(e -> {
            pret.setDatePret(datePretPicker.getValue());
            pret.setDateRetourPrevu(dateRetourPrevPicker.getValue());
            pret.setDateRetourEffective(dateRetourEffPicker.getValue());
            pret.setStatut(statutCombo.getValue());

            boolean success = PretDAO.modifierPret(pret);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prêt modifié avec succès !");
                alert.showAndWait();
                new PretListController().showPretList(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification du prêt !");
                alert.showAndWait();
            }
        });

        Button cancelBtn = new Button("🚪 Annuler");
        cancelBtn.setStyle("-fx-font-size: 14px; -fx-pref-width: 350px;-fx-background-radius: 8px; " +
                "-fx-background-color: #64748b; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelBtn.setOnAction(e -> new PretListController().showPretList(primaryStage));

        HBox buttonBox = new HBox(15, cancelBtn, updateBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        return buttonBox;
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
