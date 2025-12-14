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
import java.util.stream.Collectors;

public class publicSearchController {

    private TableView<Document> tableView;
    private DocumentDAO documentDAO;

    public void showDocumentSearch(Stage primaryStage) {
        primaryStage.setTitle("Recherche de Documents - SYGEBIB");
        documentDAO = new DocumentDAO();

        HBox header = createHeader("🔎 Recherche de Documents");

        HBox toolbar = createToolbar(primaryStage);


      
        tableView = createDocumentsTable();

       
        HBox statusBar = createStatusBar();

     
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(new VBox(header, toolbar));
        StackPane tableContainer = new StackPane(tableView);
        tableContainer.setPadding(new Insets(15));
        mainLayout.setCenter(tableContainer);
        mainLayout.setBottom(statusBar);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: #f8fafc;");

        Scene scene = new Scene(mainLayout, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

     
        loadDocuments();
    }

    private HBox createHeader(String titleText) {
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        HBox header = new HBox(titleLabel);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
        return header;
    }

    private HBox createToolbar(Stage primaryStage) {
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par titre, auteur ou thème...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 10px 12px; -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");

        ComboBox<String> typeDropdown = new ComboBox<>();
        typeDropdown.setPromptText("Filtrer par type");
        typeDropdown.setPrefWidth(180);
        typeDropdown.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");

        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.setPromptText("Filtrer par statut");
        statusDropdown.setPrefWidth(150);
        statusDropdown.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");

        try {
            List<String> types = documentDAO.getAllDocuments().stream()
                    .map(d -> d.getTypeDocument().name())
                    .distinct()
                    .collect(Collectors.toList());
            typeDropdown.setItems(FXCollections.observableArrayList(types));

            List<String> statusList = documentDAO.getAllDocuments().stream()
                    .map(Document::getDisponible)
                    .distinct()
                    .collect(Collectors.toList());
            statusDropdown.setItems(FXCollections.observableArrayList(statusList));
        } catch (SQLException e) {
            e.printStackTrace();
        }

   
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                filterDocuments(newVal, typeDropdown.getValue(), statusDropdown.getValue()));
        typeDropdown.valueProperty().addListener((obs, oldVal, newVal) ->
                filterDocuments(searchField.getText(), newVal, statusDropdown.getValue()));
        statusDropdown.valueProperty().addListener((obs, oldVal, newVal) ->
                filterDocuments(searchField.getText(), typeDropdown.getValue(), newVal));

   
        Button refreshStatusBtn = new Button("🔄");
        refreshStatusBtn.setStyle("-fx-font-size: 14px;");
        refreshStatusBtn.setOnAction(e -> {
            statusDropdown.getSelectionModel().clearSelection(); 
            filterDocuments(searchField.getText(), typeDropdown.getValue(), null); 
        });

      
        Button backBtn = new Button("🏠 Menu Principal");
        backBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #475569; -fx-text-fill: white;");
        backBtn.setOnAction(e -> new MainMenuController().showMainMenu(primaryStage));

        HBox toolbar = new HBox(10, searchField, typeDropdown, statusDropdown, refreshStatusBtn, backBtn);
        toolbar.setPadding(new Insets(15, 30, 15, 30));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
        return toolbar;
    }


 private TableView<Document> createDocumentsTable() {
    TableView<Document> table = new TableView<>();
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.setStyle(
        "-fx-background-color: white; " +
        "-fx-border-color: #e2e8f0; " +
        "-fx-border-radius: 8px; " +
        "-fx-background-radius: 8px; " +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);"
    );

 
    table.getColumns().addAll(
        createColumn("Titre", "titre", 250),
        createColumn("Auteur", "auteur", 150),
        createColumn("Thème", "theme", 150),
        createColumn("Type", "typeDocument", 120),
        createStatusColumn("Statut", "disponible", 120)
    );

  
    table.setRowFactory(tv -> {
        TableRow<Document> row = new TableRow<>();
        row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !row.isEmpty()) {
                row.setStyle("-fx-background-color: #f1f5f9;");
            } else {
                row.setStyle("");
            }
        });
        return row;
    });

    return table;
}

private <T> TableColumn<Document, T> createColumn(String title, String property, double width) {
    TableColumn<Document, T> col = new TableColumn<>(title);
    col.setPrefWidth(width);
    col.setCellValueFactory(new PropertyValueFactory<>(property));
    col.setStyle(
        "-fx-alignment: CENTER-LEFT; " +
        "-fx-font-size: 13px; " +
        "-fx-padding: 10px;"
    );
    return col;
}

private TableColumn<Document, String> createStatusColumn(String title, String property, double width) {
    TableColumn<Document, String> col = new TableColumn<>(title);
    col.setPrefWidth(width);
    col.setCellValueFactory(new PropertyValueFactory<>(property));
    col.setCellFactory(column -> new TableCell<Document, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(item);
                if ("disponible".equalsIgnoreCase(item)) {
                    setStyle("-fx-text-fill: #059669; -fx-font-weight: bold; -fx-alignment: CENTER;");
                } else {
                    setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-alignment: CENTER;");
                }
            }
        }
    });
    return col;
}

    private HBox createStatusBar() {
        Label status = new Label("SYGEBIB - Système de Gestion de Bibliothèque");
        status.setPadding(new Insets(10));
        status.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        HBox statusBar = new HBox(status);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #f1f5f9; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");
        return statusBar;
    }

    private void loadDocuments() {
        try {
            List<Document> docs = documentDAO.getAllDocuments();
            tableView.setItems(FXCollections.observableArrayList(docs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterDocuments(String search, String type, String status) {
        try {
            List<Document> allDocs = documentDAO.getAllDocuments();
            List<Document> filtered = allDocs.stream()
                    .filter(d -> (search == null || search.isEmpty() ||
                            d.getTitre().toLowerCase().contains(search.toLowerCase()) ||
                            d.getAuteur().toLowerCase().contains(search.toLowerCase()) ||
                            d.getTheme().toLowerCase().contains(search.toLowerCase())))
                    .filter(d -> (type == null || type.isEmpty() || d.getTypeDocument().name().equals(type)))
                    .filter(d -> (status == null || status.isEmpty() || d.getDisponible().equalsIgnoreCase(status)))
                    .collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filtered));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
