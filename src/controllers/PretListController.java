package controllers;

import dao.PretDAO;
import models.Pret;
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

public class PretListController {

    private TableView<Pret> tableView;
    private PretDAO pretDAO;
    private Label countLabel;

    public void showPretList(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Prêts - SYGEBIB");
        pretDAO = new PretDAO();

   
        HBox headerBox = createHeader("📚 GESTION DES PRÊTS",
                "Gérez les prêts de documents aux abonnés");

 
        HBox toolbar = createToolbar(primaryStage);

      
        tableView = createPretsTable(primaryStage);

    
        HBox statusBar = createStatusBar();

     
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createMainContainer(headerBox, toolbar));
        mainLayout.setCenter(createTableContainer(tableView));
        mainLayout.setBottom(statusBar);
        mainLayout.setStyle("-fx-background-color: #f8fafc;");

        loadPrets();

    
        setupFullScreen(primaryStage, mainLayout);
    }

    private void setupFullScreen(Stage primaryStage, BorderPane mainLayout) {
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);

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
        searchField.setPromptText("Rechercher par abonné ou document...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12px 15px; -fx-background-radius: 8px; " +
                "-fx-border-radius: 8px; -fx-border-color: #cbd5e1; -fx-background-color: white;");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPrets(newValue);
        });

        Button addBtn = createActionButton("➕ Nouveau Prêt", "#059669", primaryStage, "add");
        Button backBtn = createActionButton("🏠 Menu Principal", "#475569", primaryStage, "back");
        

        countLabel = new Label("0 prêt(s)");
        countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(20);
        toolbar.setPadding(new Insets(20, 30, 20, 30));
        toolbar.setStyle("-fx-background-color: white;");
        toolbar.getChildren().addAll(new Label("🔍"), searchField, addBtn, backBtn, spacer, countLabel);
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
				try {
					new AddPretController().showAddPret(primaryStage);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    break;
                case "back":
                    new MainMenuController().showMainMenu(primaryStage);
                    break;
            }
        });

        return button;
    }

    private TableView<Pret> createPretsTable(Stage primaryStage) {
        TableView<Pret> table = new TableView<>();
        table.setStyle("-fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String columnStyle = "-fx-alignment: CENTER-LEFT; -fx-font-size: 13px; -fx-padding: 12px 8px;";
        String headerStyle = "-fx-background-color: #f1f5f9; -fx-font-weight: bold; -fx-text-fill: #475569;";

        TableColumn<Pret, String> abonneCol = createTableColumn("ABONNÉ", "abonne", 150, columnStyle, headerStyle);
        TableColumn<Pret, String> documentCol = createTableColumn("DOCUMENT", "document", 150, columnStyle, headerStyle);
        TableColumn<Pret, String> datePretCol = createTableColumn("DATE PRÊT", "datePret", 120, columnStyle, headerStyle);
        TableColumn<Pret, String> dateRetourPrevCol = createTableColumn("DATE RETOUR PRÉVU", "dateRetourPrevu", 150, columnStyle, headerStyle);
        TableColumn<Pret, String> dateRetourEffCol = createTableColumn("DATE RETOUR EFFECTIVE", "dateRetourEffective", 150, columnStyle, headerStyle);
        TableColumn<Pret, String> statutCol = createTableColumn("STATUT", "statut", 100, columnStyle, headerStyle);

        TableColumn<Pret, Void> actionCol = new TableColumn<>("ACTIONS");
        actionCol.setPrefWidth(180);
        actionCol.setStyle(headerStyle);
        actionCol.setCellFactory(param -> createActionCell(primaryStage));

        table.getColumns().addAll(abonneCol, documentCol, datePretCol, dateRetourPrevCol, dateRetourEffCol, statutCol, actionCol);

        return table;
    }

    private TableCell<Pret, Void> createActionCell(Stage primaryStage) {
        return new TableCell<Pret, Void>() {
            private final HBox buttonBox = new HBox(5);
            private final Button viewBtn = createIconButton("👁", "#3b82f6");
            private final Button editBtn = createIconButton("✏", "#f59e0b");
            private final Button deleteBtn = createIconButton("🗑", "#ef4444");

            {
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.setStyle("-fx-padding: 5px;");

                viewBtn.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    viewPretDetails(pret);
                });

                editBtn.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    try {
						new EditPretController().showEditPret(primaryStage,pret);
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
                });

                deleteBtn.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    deletePret(pret);
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

    private <T> TableColumn<Pret, T> createTableColumn(String title, String property, double width,
                                                      String cellStyle, String headerStyle) {
        TableColumn<Pret, T> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setStyle(headerStyle);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(tc -> new TableCell<Pret, T>() {
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

    private StackPane createTableContainer(TableView<Pret> table) {
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

    private void loadPrets() {
        try {
            List<Pret> prets = pretDAO.getAllPrets();
            ObservableList<Pret> observableList = FXCollections.observableArrayList(prets);
            tableView.setItems(observableList);
            countLabel.setText(prets.size() + " prêt(s)");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des prêts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchPrets(String keyword) {
        try {
            List<Pret> filteredList;
            if (keyword == null || keyword.trim().isEmpty()) {
                filteredList = pretDAO.getAllPrets();
            } else {
                String lowerKeyword = keyword.toLowerCase();

                filteredList = pretDAO.getAllPrets().stream().filter(pret -> {
                    // Recherche sur abonné et document
                    boolean matchesText = pret.getAbonne().toLowerCase().contains(lowerKeyword)
                            || pret.getDocument().toLowerCase().contains(lowerKeyword);

                    // Recherche sur les dates (format yyyy-MM-dd)
                    boolean matchesDatePret = pret.getDatePret() != null && pret.getDatePret().toString().contains(lowerKeyword);
                    boolean matchesDateRetourPrevu = pret.getDateRetourPrevu() != null && pret.getDateRetourPrevu().toString().contains(lowerKeyword);
                    boolean matchesDateRetourEffective = pret.getDateRetourEffective() != null && pret.getDateRetourEffective().toString().contains(lowerKeyword);

                    // Recherche sur statut
                    boolean matchesStatut = pret.getStatut() != null && pret.getStatut().toLowerCase().contains(lowerKeyword);

                    return matchesText || matchesDatePret || matchesDateRetourPrevu || matchesDateRetourEffective || matchesStatut;
                }).toList();
            }

            tableView.setItems(FXCollections.observableArrayList(filteredList));
            countLabel.setText(filteredList.size() + " prêt(s)");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }


    private void viewPretDetails(Pret pret) {
        StringBuilder details = new StringBuilder();
        details.append("Abonné: ").append(pret.getAbonne()).append("\n");
        details.append("Document: ").append(pret.getDocument()).append("\n");
        details.append("Date de prêt: ").append(pret.getDatePret()).append("\n");
        details.append("Date retour prévu: ").append(pret.getDateRetourPrevu()).append("\n");
        details.append("Date retour effective: ").append(pret.getDateRetourEffective()).append("\n");
        details.append("Statut: ").append(pret.getStatut()).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Prêt");
        alert.setHeaderText("📚 Prêt de " + pret.getDocument());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefSize(400, 300);
        alert.showAndWait();
    }

    private void deletePret(Pret pret) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer le prêt");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce prêt ? Cette action est irréversible !");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pretDAO.deletePret(pret.getId());
                    loadPrets();
                    showAlert("Succès", "✅ Prêt supprimé avec succès");
                } catch (Exception e) {
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
