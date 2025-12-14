package controllers;

import dao.AbonneDAO;
import models.Abonne;
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

public class AbonneListController {
    
    private TableView<Abonne> tableView;
    private AbonneDAO abonneDAO;
    private Label countLabel;
    
    public void showAbonneList(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Abonnés - SYGEBIB");
        abonneDAO = new AbonneDAO();
        
        HBox headerBox = createHeader("👥 GESTION DES ABONNÉS", 
            "Gérez les abonnés, inscriptions et informations personnelles");
        
        HBox toolbar = createToolbar(primaryStage);
        
        tableView = createAbonnesTable(primaryStage);
        
        HBox statusBar = createStatusBar();
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createMainContainer(headerBox, toolbar));
        mainLayout.setCenter(createTableContainer(tableView));
        mainLayout.setBottom(statusBar);
        mainLayout.setStyle("-fx-background-color: #f8fafc;");
        
        loadAbonnes();
        
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
        searchField.setPromptText("Rechercher par CIN, nom, prénom ou téléphone...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-background-radius: 8px; " +
                           "-fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchAbonnes(newValue);
        });
        
  
        Button addBtn = createActionButton("➕ Nouvel Abonné", "#059669", primaryStage, "add");
        Button backBtn = createActionButton("🏠 Menu Principal", "#475569", primaryStage, "back");
        
  
        countLabel = new Label("0 abonnés");
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
                    new AddAbonneController().showAddAbonne(primaryStage);
                    break;
                case "back":
                    new MainMenuController().showMainMenu(primaryStage);
                    break;
            }
        });
        
        return button;
    }
    
    private TableView<Abonne> createAbonnesTable(Stage primaryStage) {
        TableView<Abonne> table = new TableView<>();
        table.setStyle("-fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
 
        String columnStyle = "-fx-alignment: CENTER-LEFT; -fx-font-size: 13px; -fx-padding: 12px 8px;";
        String headerStyle = "-fx-background-color: #f1f5f9; -fx-font-weight: bold; -fx-text-fill: #475569;";
        
        TableColumn<Abonne, String> cinCol = createTableColumn("CIN", "cin", 120, columnStyle, headerStyle);
        TableColumn<Abonne, String> nomCol = createTableColumn("NOM", "nom", 150, columnStyle, headerStyle);
        TableColumn<Abonne, String> prenomCol = createTableColumn("PRÉNOM", "prenom", 150, columnStyle, headerStyle);
        TableColumn<Abonne, String> adresseCol = createTableColumn("ADRESSE", "adresse", 200, columnStyle, headerStyle);
        TableColumn<Abonne, String> telephoneCol = createTableColumn("TÉLÉPHONE", "telephone", 120, columnStyle, headerStyle);
        TableColumn<Abonne, String> dateCol = createTableColumn("DATE INSCRIPTION", "dateAbonnement", 150, columnStyle, headerStyle);
        
        TableColumn<Abonne, Void> actionCol = new TableColumn<>("ACTIONS");
        actionCol.setPrefWidth(180);
        actionCol.setStyle(headerStyle);
        actionCol.setCellFactory(param -> createActionCell(primaryStage));
        
        table.getColumns().addAll(cinCol, nomCol, prenomCol, adresseCol, telephoneCol, dateCol, actionCol);
        return table;
    }
    
    private TableCell<Abonne, Void> createActionCell(Stage primaryStage) {
        return new TableCell<Abonne, Void>() {
            private final HBox buttonBox = new HBox(5);
            private final Button viewBtn = createIconButton("👁", "#3b82f6");
            private final Button editBtn = createIconButton("✏", "#f59e0b");
            private final Button deleteBtn = createIconButton("🗑", "#ef4444");
            
            {
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.setStyle("-fx-padding: 5px;");
                
                viewBtn.setOnAction(event -> {
                    Abonne abonne = getTableView().getItems().get(getIndex());
                    viewAbonneDetails(abonne);
                });
                
                editBtn.setOnAction(event -> {
                    Abonne abonne = getTableView().getItems().get(getIndex());
                    new EditAbonneController().showEditAbonne(primaryStage, abonne);
                });
                
                deleteBtn.setOnAction(event -> {
                    Abonne abonne = getTableView().getItems().get(getIndex());
                    deleteAbonne(abonne);
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
    
    private <T> TableColumn<Abonne, T> createTableColumn(String title, String property, double width, 
                                                        String cellStyle, String headerStyle) {
        TableColumn<Abonne, T> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setStyle(headerStyle);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(tc -> new TableCell<Abonne, T>() {
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
    
    private StackPane createTableContainer(TableView<Abonne> table) {
        StackPane container = new StackPane(table);
        container.setPadding(new Insets(0, 30, 30, 30));
        
   
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
    
    private void loadAbonnes() {
        try {
            List<Abonne> abonnes = abonneDAO.getAllAbonnes();
            ObservableList<Abonne> observableList = FXCollections.observableArrayList(abonnes);
            tableView.setItems(observableList);
            
            countLabel.setText(abonnes.size() + " abonné(s)");
            System.out.println("✅ " + abonnes.size() + " abonnés chargés avec succès");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des abonnés: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void searchAbonnes(String keyword) {
        try {
            List<Abonne> filteredList;
            
            if (keyword == null || keyword.trim().isEmpty()) {
                filteredList = abonneDAO.getAllAbonnes();
                countLabel.setText(filteredList.size() + " abonné(s)");
            } else {
                filteredList = abonneDAO.searchAbonnes(keyword);
                countLabel.setText(filteredList.size() + " abonné(s) trouvé(s)");
            }
            
            tableView.setItems(FXCollections.observableArrayList(filteredList));
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    private void viewAbonneDetails(Abonne abonne) {
        StringBuilder details = new StringBuilder();
        details.append("CIN: ").append(abonne.getCin()).append("\n");
        details.append("Nom: ").append(abonne.getNom()).append("\n");
        details.append("Prénom: ").append(abonne.getPrenom()).append("\n");
        details.append("Adresse: ").append(abonne.getAdresse()).append("\n");
        details.append("Téléphone: ").append(abonne.getTelephone()).append("\n");
        details.append("Date d'abonnement: ").append(abonne.getDateAbonnement()).append("\n");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'Abonné");
        alert.setHeaderText("👤 " + abonne.getNom() + " " + abonne.getPrenom());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefSize(400, 300);
        alert.showAndWait();
    }
    
    private void deleteAbonne(Abonne abonne) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer l'abonné");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer l'abonné : \n\n\"" + 
                             abonne.getNom() + " " + abonne.getPrenom() + "\"\n\nCette action est irréversible !");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    abonneDAO.deleteAbonne(abonne.getId());
                    loadAbonnes();
                    showAlert("Succès", "✅ Abonné supprimé avec succès");
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