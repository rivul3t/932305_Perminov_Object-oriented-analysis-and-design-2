package com.warehouse.ui;

import com.warehouse.domain.entity.Product;
import com.warehouse.domain.entity.StockMovement;
import com.warehouse.domain.valueobject.DateRange;
import com.warehouse.domain.valueobject.Quantity;
import com.warehouse.service.WarehouseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MovementsTab {

    private final WarehouseService service = new WarehouseService();
    private final Tab tab = new Tab("Движение товаров");
    private final TableView<StockMovement> table = new TableView<>();
    private final Runnable onChanged;

    private ComboBox<Product> productCombo;
    private DatePicker pickerFrom;
    private DatePicker pickerTo;
    private Label filterErrorLabel;

    private List<StockMovement> allMovements = List.of();

    public MovementsTab(Runnable onChanged) {
        this.onChanged = onChanged;
        buildUI();
        refresh();
    }

    private void buildUI() {
        var fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        var colProduct = new TableColumn<StockMovement, String>("Товар (ID)");
        colProduct.setCellValueFactory(c ->
                new SimpleStringProperty("ID " + c.getValue().getProductId()));
        colProduct.setPrefWidth(100);

        var colType = new TableColumn<StockMovement, String>("Тип");
        colType.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getType() == StockMovement.Type.IN ? "Приход" : "Списание"));
        colType.setPrefWidth(90);

        var colQty = new TableColumn<StockMovement, String>("Количество");
        colQty.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getQuantity().toString()));
        colQty.setPrefWidth(100);

        var colDate = new TableColumn<StockMovement, String>("Дата");
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMovedAt().format(fmt)));
        colDate.setPrefWidth(130);

        var colComment = new TableColumn<StockMovement, String>("Комментарий");
        colComment.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getComment() != null ? c.getValue().getComment() : ""));
        colComment.setPrefWidth(200);

        table.getColumns().addAll(colProduct, colType, colQty, colDate, colComment);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // --- Форма добавления движения ---
        productCombo = new ComboBox<>();
        productCombo.setPromptText("Выберите товар");
        productCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCode() + " — " + item.getName());
            }
        });
        productCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCode() + " — " + item.getName());
            }
        });
        productCombo.setPrefWidth(250);

        var qtyField = new TextField();
        qtyField.setPromptText("Количество");
        qtyField.setPrefWidth(80);

        var unitCombo = new ComboBox<String>();
        unitCombo.getItems().addAll("шт", "кг", "л");
        unitCombo.setValue("шт");

        var typeToggle = new ToggleGroup();
        var btnIn = new RadioButton("Приход");
        var btnOut = new RadioButton("Списание");
        btnIn.setToggleGroup(typeToggle);
        btnOut.setToggleGroup(typeToggle);
        btnIn.setSelected(true);

        var commentField = new TextField();
        commentField.setPromptText("Комментарий (необязательно)");
        commentField.setPrefWidth(200);

        var btnSave = new Button("Добавить");
        btnSave.setOnAction(e -> {
            try {
                Product selected = productCombo.getValue();
                if (selected == null) throw new IllegalStateException("Выберите товар");
                int qty = Integer.parseInt(qtyField.getText().trim());
                var quantity = new Quantity(qty, unitCombo.getValue());

                if (btnIn.isSelected()) {
                    service.receiveStock(selected.getId(), quantity, commentField.getText().trim());
                } else {
                    service.writeOffStock(selected.getId(), quantity, commentField.getText().trim());
                }

                qtyField.clear();
                commentField.clear();
                refresh();
                onChanged.run();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        var addForm = new HBox(8,
                new Label("Товар:"), productCombo,
                new Label("Кол-во:"), qtyField, unitCombo,
                btnIn, btnOut,
                new Label("Комм.:"), commentField,
                btnSave
        );
        addForm.setPadding(new Insets(8));
        addForm.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        pickerFrom = new DatePicker(LocalDate.now().minusMonths(1));
        pickerTo = new DatePicker(LocalDate.now());
        filterErrorLabel = new Label();
        filterErrorLabel.setStyle("-fx-text-fill: red;");

        var btnApplyFilter = new Button("Применить");
        btnApplyFilter.setOnAction(e -> applyFilter());

        var btnResetFilter = new Button("Сбросить");
        btnResetFilter.setOnAction(e -> {
            pickerFrom.setValue(null);
            pickerTo.setValue(null);
            filterErrorLabel.setText("");
            table.setItems(FXCollections.observableArrayList(allMovements));
        });

        var filterBar = new HBox(8,
                new Label("Период:"), pickerFrom, new Label("—"), pickerTo,
                btnApplyFilter, btnResetFilter, filterErrorLabel
        );
        filterBar.setPadding(new Insets(4, 8, 4, 8));
        filterBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        var root = new VBox(addForm, new Separator(), filterBar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        tab.setContent(root);
    }

    private void applyFilter() {
        filterErrorLabel.setText("");
        LocalDate from = pickerFrom.getValue();
        LocalDate to = pickerTo.getValue();

        if (from == null || to == null) {
            filterErrorLabel.setText("Укажите обе даты");
            return;
        }

        try {
            var range = new DateRange(from, to);
            var filtered = allMovements.stream()
                    .filter(m -> range.contains(m.getMovedAt()))
                    .toList();
            table.setItems(FXCollections.observableArrayList(filtered));
        } catch (IllegalArgumentException e) {
            filterErrorLabel.setText(e.getMessage());
        }
    }

    public void refresh() {
        try {
            var products = new WarehouseService().getAllProducts();
            productCombo.setItems(FXCollections.observableArrayList(products));
            allMovements = service.getAllMovements();
            table.setItems(FXCollections.observableArrayList(allMovements));
            filterErrorLabel.setText("");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public Tab getTab() { return tab; }
}
