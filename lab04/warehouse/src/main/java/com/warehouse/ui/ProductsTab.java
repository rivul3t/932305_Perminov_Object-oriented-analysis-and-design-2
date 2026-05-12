package com.warehouse.ui;

import com.warehouse.domain.entity.Product;
import com.warehouse.domain.valueobject.Currency;
import com.warehouse.domain.valueobject.Price;
import com.warehouse.domain.valueobject.ProductCode;
import com.warehouse.domain.valueobject.Weight;
import com.warehouse.service.WarehouseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductsTab {

    private final WarehouseService service = new WarehouseService();
    private final Tab tab = new Tab("Товары");
    private final TableView<Product> table = new TableView<>();

    public ProductsTab() {
        buildUI();
        refresh();
    }

    private void buildUI() {
        // Колонки таблицы
        var colCode = new TableColumn<Product, String>("Код");
        colCode.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode().toString()));
        colCode.setPrefWidth(100);

        var colName = new TableColumn<Product, String>("Название");
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colName.setPrefWidth(200);

        var colPrice = new TableColumn<Product, String>("Цена");
        colPrice.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrice().toString()));
        colPrice.setPrefWidth(120);

        var colWeight = new TableColumn<Product, String>("Вес");
        colWeight.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getWeight().toString()));
        colWeight.setPrefWidth(100);

        var colDesc = new TableColumn<Product, String>("Описание");
        colDesc.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDescription() != null ? c.getValue().getDescription() : ""));
        colDesc.setPrefWidth(250);

        table.getColumns().addAll(colCode, colName, colPrice, colWeight, colDesc);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Строка поиска
        var searchField = new TextField();
        searchField.setPromptText("Поиск по названию или коду...");
        searchField.textProperty().addListener((obs, old, val) -> {
            try {
                List<Product> results = val.isBlank()
                        ? service.getAllProducts()
                        : service.searchProducts(val);
                table.setItems(FXCollections.observableArrayList(results));
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        // Кнопки
        var btnAdd = new Button("Добавить");
        var btnEdit = new Button("Редактировать");
        var btnDelete = new Button("Удалить");

        btnAdd.setOnAction(e -> showProductDialog(null));
        btnEdit.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showProductDialog(selected);
        });
        btnDelete.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            var confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Удалить товар \"" + selected.getName() + "\"?");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        service.deleteProduct(selected.getId());
                        refresh();
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                }
            });
        });

        var buttons = new HBox(8, btnAdd, btnEdit, btnDelete);
        var topBar = new HBox(8, searchField, buttons);
        topBar.setPadding(new Insets(8));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        var root = new VBox(topBar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        tab.setContent(root);
    }

    private void showProductDialog(Product existing) {
        var dialog = new Dialog<Product>();
        dialog.setTitle(existing == null ? "Добавить товар" : "Редактировать товар");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        var fName = new TextField();
        var fCode = new TextField();
        fCode.setPromptText("ABC-1234");
        var fPrice = new TextField();
        var fCurrency = new ComboBox<String>();
        fCurrency.getItems().addAll("RUB", "USD", "EUR");
        fCurrency.setValue("RUB");
        var fWeightVal = new TextField();
        var fWeightUnit = new ComboBox<String>();
        fWeightUnit.getItems().addAll("кг", "г");
        fWeightUnit.setValue("кг");
        var fDesc = new TextField();

        if (existing != null) {
            fName.setText(existing.getName());
            fCode.setText(existing.getCode().value());
            fPrice.setText(existing.getPrice().amount().toPlainString());
            fCurrency.setValue(existing.getPrice().currency().code());
            fWeightVal.setText(existing.getWeight().value().toPlainString());
            fWeightUnit.setValue(existing.getWeight().unit());
            fDesc.setText(existing.getDescription() != null ? existing.getDescription() : "");
        }

        var errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        grid.addRow(0, new Label("Название:"), fName);
        grid.addRow(1, new Label("Код (ABC-1234):"), fCode);
        grid.addRow(2, new Label("Цена:"), new HBox(4, fPrice, fCurrency));
        grid.addRow(3, new Label("Вес:"), new HBox(4, fWeightVal, fWeightUnit));
        grid.addRow(4, new Label("Описание:"), fDesc);
        grid.add(errorLabel, 0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);

        var okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            e.consume();
            try {
                var product = new Product(
                    existing != null ? existing.getId() : null,
                    fName.getText().trim(),
                    new ProductCode(fCode.getText().trim()),
                    new Price(new BigDecimal(fPrice.getText().trim()), new Currency(fCurrency.getValue())),
                    new Weight(new BigDecimal(fWeightVal.getText().trim()), fWeightUnit.getValue()),
                    fDesc.getText().trim()
                );
                service.saveProduct(product);
                refresh();
                dialog.close();
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        dialog.showAndWait();
    }

    public void refresh() {
        try {
            table.setItems(FXCollections.observableArrayList(service.getAllProducts()));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public Tab getTab() { return tab; }

    public List<Product> getProducts() {
        try {
            return service.getAllProducts();
        } catch (Exception e) {
            return List.of();
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
