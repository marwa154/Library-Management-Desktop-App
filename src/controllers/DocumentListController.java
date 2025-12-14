package controllers;

import dao.DocumentDAO;
import models.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class DocumentListController {
    
    private TableView<Document> tableView;
    private DocumentDAO documentDAO;
    private Label countLabel;
    
    public void showDocumentList(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Documents - SYGEBIB");
        documentDAO = new DocumentDAO();
        
        HBox headerBox = createHeader("📚 GESTION DES DOCUMENTS", 
            "Gérez votre catalogue de documents, livres et ressources");
        
        HBox toolbar = createToolbar(primaryStage);
        
        tableView = createDocumentsTable(primaryStage);
        
        HBox statusBar = createStatusBar();
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createMainContainer(headerBox, toolbar));
        mainLayout.setCenter(createTableContainer(tableView));
        mainLayout.setBottom(statusBar);
        mainLayout.setStyle("-fx-background-color: #f8fafc;");
        
        loadDocuments();
        
        setupFullScreen(primaryStage, mainLayout);
    }
    
    private void setupFullScreen(Stage primaryStage, BorderPane mainLayout) {
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        
        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        
        primaryStage.setMaximized(true);
        
        tableView.prefHeightProperty().bind(primaryStage.heightProperty().subtract(250));
        tableView.prefWidthProperty().bind(primaryStage.widthProperty().subtract(60));
        
        primaryStage.show();
    }
    
    private HBox createHeader(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        
        VBox textBox = new VBox(5, titleLabel, subtitleLabel);
        
        HBox headerBox = new HBox(textBox);
        headerBox.setPadding(new Insets(25, 30, 25, 30));
        headerBox.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        return headerBox;
    }
    
    private HBox createToolbar(Stage primaryStage) {
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par titre, auteur, cote ou thème...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-background-radius: 8px; " +
                           "-fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDocuments(newValue);
        });
        
        Button addBtn = createActionButton("➕ Nouveau Document", "#059669", primaryStage, "add");
        Button backBtn = createActionButton("🏠 Menu Principal", "#475569", primaryStage, "back");
        
        countLabel = new Label("0 documents");
        countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox toolbar = new HBox(20);
        toolbar.setPadding(new Insets(20, 30, 20, 30));
        toolbar.setStyle("-fx-background-color: white;");
        toolbar.getChildren().addAll(
            new Label("🔍"), searchField, addBtn, backBtn, spacer, countLabel
        );
        toolbar.setAlignment(Pos.CENTER_LEFT);
        
        return toolbar;
    }
    
    private Button createActionButton(String text, String color, Stage primaryStage, String type) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 12px 20px; -fx-background-radius: 8px; " +
                       "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand;");
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-size: 14px; -fx-padding: 12px 20px; -fx-background-radius: 8px; " +
            "-fx-background-color: " + darkenColor(color) + "; -fx-text-fill: white; -fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-size: 14px; -fx-padding: 12px 20px; -fx-background-radius: 8px; " +
            "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand;"
        ));
        
        button.setOnAction(e -> {
            switch (type) {
                case "add":
                    new AddDocumentController().showAddDocument(primaryStage);
                    break;
                case "back":
                    new MainMenuController().showMainMenu(primaryStage);
                    break;
            }
        });
        
        return button;
    }
    
    private TableView<Document> createDocumentsTable(Stage primaryStage) {
        TableView<Document> table = new TableView<>();
        table.setStyle("-fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        String columnStyle = "-fx-alignment: CENTER-LEFT; -fx-font-size: 13px; -fx-padding: 12px 8px;";
        String headerStyle = "-fx-background-color: #f1f5f9; -fx-font-weight: bold; -fx-text-fill: #475569;";
        
        TableColumn<Document, String> coteCol = createTableColumn("COTE", "cote", 120, columnStyle, headerStyle);
        TableColumn<Document, String> titreCol = createTableColumn("TITRE", "titre", 250, columnStyle, headerStyle);
        TableColumn<Document, String> auteurCol = createTableColumn("AUTEUR", "auteur", 180, columnStyle, headerStyle);
        TableColumn<Document, String> themeCol = createTableColumn("THÈME", "theme", 150, columnStyle, headerStyle);
        TableColumn<Document, String> typeCol = createTableColumn("TYPE", "typeDocument", 100, columnStyle, headerStyle);
        
        TableColumn<Document, String> dispoCol = createTableColumn("STATUT", "disponible", 120, columnStyle, headerStyle);
        dispoCol.setCellFactory(column -> new TableCell<Document, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("disponible".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        TableColumn<Document, Void> actionCol = new TableColumn<>("ACTIONS");
        actionCol.setPrefWidth(180);
        actionCol.setStyle(headerStyle);
        actionCol.setCellFactory(param -> createActionCell(primaryStage));
        
        table.getColumns().addAll(coteCol, titreCol, auteurCol, themeCol, typeCol, dispoCol, actionCol);
        return table;
    }
    
    private TableCell<Document, Void> createActionCell(Stage primaryStage) {
        return new TableCell<Document, Void>() {
            private final HBox buttonBox = new HBox(5);
            private final Button viewBtn = createIconButton("👁", "#3b82f6");
            private final Button editBtn = createIconButton("✏", "#f59e0b");
            private final Button deleteBtn = createIconButton("🗑", "#ef4444");
            
            {
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.setStyle("-fx-padding: 5px;");
                
                viewBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    viewDocumentDetails(doc);
                });
                
                editBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    new EditDocumentController().showEditDocument(primaryStage, doc);
                });
                
                deleteBtn.setOnAction(event -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    deleteDocument(doc);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    buttonBox.getChildren().setAll(viewBtn, editBtn, deleteBtn);
                    setGraphic(buttonBox);
                }
            }
        };
    }
    
    private Button createIconButton(String icon, String color) {
        Button button = new Button(icon);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 12px; " +
                       "-fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 6px; -fx-cursor: hand;");
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + darkenColor(color) + "; -fx-text-fill: white; -fx-font-size: 12px; " +
            "-fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 6px; -fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 12px; " +
            "-fx-padding: 8px; -fx-min-width: 36px; -fx-min-height: 36px; -fx-background-radius: 6px; -fx-cursor: hand;"
        ));
        
        return button;
    }
    
    private <T> TableColumn<Document, T> createTableColumn(String title, String property, double width, 
                                                          String cellStyle, String headerStyle) {
        TableColumn<Document, T> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setStyle(headerStyle);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(tc -> new TableCell<Document, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
                setStyle(cellStyle);
            }
        });
        return column;
    }
    
    private VBox createMainContainer(HBox header, HBox toolbar) {
        VBox container = new VBox();
        container.getChildren().addAll(header, toolbar);
        return container;
    }
    
    private StackPane createTableContainer(TableView<Document> table) {
        StackPane container = new StackPane(table);
        container.setPadding(new Insets(0, 30, 30, 30));
        
        // Permettre au tableau de s'étendre dans tout le conteneur
        StackPane.setAlignment(table, Pos.CENTER);
        StackPane.setMargin(table, Insets.EMPTY);
        
        return container;
    }
    
    private HBox createStatusBar() {
        Label statusLabel = new Label("SYGEBIB - Système de Gestion de Bibliothèque");
        statusLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        
        HBox statusBar = new HBox(statusLabel);
        statusBar.setPadding(new Insets(15, 30, 15, 30));
        statusBar.setStyle("-fx-background-color: #f1f5f9; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");
        statusBar.setAlignment(Pos.CENTER_LEFT);
        
        return statusBar;
    }
    
    private String darkenColor(String hexColor) {
        return hexColor.replaceFirst("#", "#80");
    }
    
    private void loadDocuments() {
        try {
            List<Document> documents = documentDAO.getAllDocuments();
            ObservableList<Document> observableList = FXCollections.observableArrayList(documents);
            tableView.setItems(observableList);
            
            countLabel.setText(documents.size() + " document(s)");
            System.out.println("✅ " + documents.size() + " documents chargés avec succès");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des documents: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void searchDocuments(String keyword) {
        try {
            List<Document> allDocuments = documentDAO.getAllDocuments();
            ObservableList<Document> filteredList = FXCollections.observableArrayList();
            
            if (keyword == null || keyword.trim().isEmpty()) {
                tableView.setItems(FXCollections.observableArrayList(allDocuments));
                countLabel.setText(allDocuments.size() + " document(s)");
                return;
            }
            
            for (Document doc : allDocuments) {
                if (doc.getTitre().toLowerCase().contains(keyword.toLowerCase()) ||
                    doc.getAuteur().toLowerCase().contains(keyword.toLowerCase()) ||
                    doc.getCote().toLowerCase().contains(keyword.toLowerCase()) ||
                    doc.getTheme().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(doc);
                }
            }
            tableView.setItems(filteredList);
            countLabel.setText(filteredList.size() + " document(s) trouvé(s)");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    private void viewDocumentDetails(Document doc) {
        StringBuilder details = new StringBuilder();
        details.append("Auteur: ").append(doc.getAuteur()).append("\n");
        details.append("Cote: ").append(doc.getCote()).append("\n");
        details.append("Type: ").append(doc.getTypeDocument()).append("\n");
        details.append("Thème: ").append(doc.getTheme()).append("\n");
        details.append("Disponibilité: ").append(doc.getDisponible()).append("\n");
        
        if (doc.getIsbn() != null && !doc.getIsbn().isEmpty()) {
            details.append("ISBN: ").append(doc.getIsbn()).append("\n");
        }
        
        if (doc.getEditeur() != null && !doc.getEditeur().isEmpty()) {
            details.append("Éditeur: ").append(doc.getEditeur()).append("\n");
        }
        
        if (doc.getDateParution() != null) {
            details.append("Date de parution: ").append(doc.getDateParution()).append("\n");
        }
        
        if (doc.getAnneeEtude() != null) {
            details.append("Année d'étude: ").append(doc.getAnneeEtude()).append("\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Document");
        alert.setHeaderText("📖 " + doc.getTitre());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefSize(400, 300);
        alert.showAndWait();
    }
    
    private void deleteDocument(Document doc) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer le document");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer le document : \n\n\"" + doc.getTitre() + "\"\n\nCette action est irréversible !");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    documentDAO.deleteDocument(doc.getId());
                    loadDocuments();
                    showAlert("Succès", "✅ Document supprimé avec succès");
                } catch (SQLException e) {
                    showAlert("Erreur", "❌ Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}