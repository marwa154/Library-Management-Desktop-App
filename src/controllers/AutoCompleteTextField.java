package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.util.Callback;

public class AutoCompleteTextField<T> extends TextField {
    
    private final ObservableList<T> items;
    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();
    private ListView<T> listView;
    private Popup popup;
    private Converter<T> converter;
    
    public interface Converter<T> {
        String toString(T item);
        boolean matches(T item, String searchText);
    }
    
    public AutoCompleteTextField() {
        this(FXCollections.observableArrayList());
    }
    
    public AutoCompleteTextField(ObservableList<T> items) {
        this.items = FXCollections.observableArrayList(items);
        setupAutoComplete();
    }
    
    private void setupAutoComplete() {
        // Créer la ListView stylisée
        listView = new ListView<>();
        listView.setItems(items);
        listView.setPrefWidth(this.getPrefWidth() > 0 ? this.getPrefWidth() : 350);
        listView.setMaxHeight(200);
        
        // Style CSS pour la ListView
        listView.setStyle("-fx-background-color: white; " +
                         "-fx-border-color: #e2e8f0; " +
                         "-fx-border-width: 1px; " +
                         "-fx-border-radius: 8px; " +
                         "-fx-background-radius: 8px; " +
                         "-fx-padding: 2px;");
        
        // Créer un conteneur pour la popup avec ombre
        StackPane popupContainer = new StackPane(listView);
        popupContainer.setStyle("-fx-background-color: transparent; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        popupContainer.setPadding(new Insets(5));
        
        // Créer la popup
        popup = new Popup();
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.getContent().add(popupContainer);
        
        // Gestion des événements de saisie
        textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                popup.hide();
                selectedItem.set(null);
            } else {
                filterItems(newVal);
                if (!popup.isShowing() && !listView.getItems().isEmpty()) {
                    showPopup();
                } else if (listView.getItems().isEmpty()) {
                    popup.hide();
                }
            }
        });
        
        // Fermer la popup quand on perd le focus
        focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Petit délai pour permettre la sélection avec la souris
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    javafx.application.Platform.runLater(() -> {
                        if (!listView.isFocused()) {
                            popup.hide();
                        }
                    });
                }).start();
            }
        });
        
        // Gestion du clic sur un élément
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 1) {
                selectItem();
            }
        });
        
        // Style des cellules
        listView.setCellFactory(lv -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    if (converter != null) {
                        setText(converter.toString(item));
                    } else {
                        setText(item.toString());
                    }
                    
                    // Style pour les cellules
                    setStyle("-fx-font-size: 13px; " +
                            "-fx-padding: 8px 12px; " +
                            "-fx-background-color: transparent; " +
                            "-fx-border-color: transparent;");
                    
                    // Style pour la cellule survolée
                    setOnMouseEntered(e -> {
                        if (!isEmpty()) {
                            setStyle("-fx-font-size: 13px; " +
                                    "-fx-padding: 8px 12px; " +
                                    "-fx-background-color: #f1f5f9; " +
                                    "-fx-border-color: transparent; " +
                                    "-fx-cursor: hand;");
                        }
                    });
                    
                    setOnMouseExited(e -> {
                        if (!isEmpty()) {
                            setStyle("-fx-font-size: 13px; " +
                                    "-fx-padding: 8px 12px; " +
                                    "-fx-background-color: transparent; " +
                                    "-fx-border-color: transparent;");
                        }
                    });
                }
            }
        });
        
        // Gestion des touches
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (popup.isShowing() && !listView.getItems().isEmpty()) {
                    selectItem();
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                popup.hide();
                event.consume();
            } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                if (popup.isShowing() && !listView.getItems().isEmpty()) {
                    int index = listView.getSelectionModel().getSelectedIndex();
                    if (event.getCode() == KeyCode.DOWN) {
                        index = Math.min(index + 1, listView.getItems().size() - 1);
                    } else {
                        index = Math.max(index - 1, 0);
                    }
                    listView.getSelectionModel().select(index);
                    listView.scrollTo(index);
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.TAB) {
                if (popup.isShowing()) {
                    popup.hide();
                }
            }
        });
        
        // Quand la ListView a le focus, ne pas fermer la popup
        listView.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !this.isFocused()) {
                popup.hide();
            }
        });
    }
    
    private void showPopup() {
        if (this.getScene() == null || this.getScene().getWindow() == null) {
            return;
        }
        
        // Calculer la position
        double x = this.localToScene(0, 0).getX() + this.getScene().getWindow().getX();
        double y = this.localToScene(0, 0).getY() + this.getScene().getWindow().getY() + this.getHeight() - 5;
        
        // Ajuster si la popup dépasse l'écran en bas
        double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();
        double popupHeight = listView.getMaxHeight() + 20; // + padding et shadow
        
        if (y + popupHeight > screenHeight) {
            // Afficher au-dessus du champ
            y = this.localToScene(0, 0).getY() + this.getScene().getWindow().getY() - popupHeight + 5;
        }
        
        popup.show(this.getScene().getWindow(), x, y);
        
        // Sélectionner le premier élément
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().selectFirst();
            listView.scrollTo(0);
        }
    }
    
    private void filterItems(String searchText) {
        if (converter != null) {
            ObservableList<T> filtered = FXCollections.observableArrayList();
            for (T item : items) {
                if (converter.matches(item, searchText)) {
                    filtered.add(item);
                }
            }
            listView.setItems(filtered);
        } else {
            // Si pas de converter, utiliser toString()
            ObservableList<T> filtered = FXCollections.observableArrayList();
            for (T item : items) {
                if (item.toString().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(item);
                }
            }
            listView.setItems(filtered);
        }
    }
    
    private void selectItem() {
        T selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedItem.set(selected);
            if (converter != null) {
                setText(converter.toString(selected));
            } else {
                setText(selected.toString());
            }
            positionCaret(getText().length());
            popup.hide();
        }
    }
    
    // Getters et Setters
    public void setItems(ObservableList<T> items) {
        this.items.setAll(items);
        listView.setItems(this.items);
    }
    
    public void setConverter(Converter<T> converter) {
        this.converter = converter;
    }
    
    public T getSelectedItem() {
        return selectedItem.get();
    }
    
    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }
    
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> cellFactory) {
        listView.setCellFactory(cellFactory);
    }
  
}