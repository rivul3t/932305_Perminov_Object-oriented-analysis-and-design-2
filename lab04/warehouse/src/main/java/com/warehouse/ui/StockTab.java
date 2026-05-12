package com.warehouse.ui;

import com.warehouse.domain.entity.Product;
import com.warehouse.domain.valueobject.Price;
import com.warehouse.domain.valueobject.Quantity;
import com.warehouse.service.WarehouseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class StockTab {

    private final WarehouseService service = new WarehouseService();
    private final Tab tab = new Tab("Остатки");
    private final TableView<StockRow> table = new TableView<>();
    private final Label totalLabel = new Label();

    record StockRow(String code, String name, String quantity, String price, String total) {}

    public StockTab() {
        buildUI();
    }

    private void buildUI() {
        var colCode = new TableColumn<StockRow, String>("Код");
        colCode.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().code()));
        colCode.setPrefWidth(100);

        var colName = new TableColumn<StockRow, String>("Название");
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
        colName.setPrefWidth(200);

        var colQty = new TableColumn<StockRow, String>("Остаток");
        colQty.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().quantity()));
        colQty.setPrefWidth(110);

        var colPrice = new TableColumn<StockRow, String>("Цена за ед.");
        colPrice.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().price()));
        colPrice.setPrefWidth(120);

        var colTotal = new TableColumn<StockRow, String>("Сумма");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().total()));
        colTotal.setPrefWidth(130);

        table.getColumns().addAll(colCode, colName, colQty, colPrice, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

        var btnRefresh = new Button("Обновить");
        btnRefresh.setOnAction(e -> refresh());

        var topBar = new HBox(btnRefresh);
        topBar.setPadding(new Insets(8));

        var bottomBar = new HBox(totalLabel);
        bottomBar.setPadding(new Insets(8));

        var root = new VBox(topBar, table, bottomBar);
        VBox.setVgrow(table, Priority.ALWAYS);
        tab.setContent(root);
    }

    public void refresh() {
        try {
            var products = service.getAllProducts();
            var stockLevels = service.getAllStockLevels();
            var rows = new ArrayList<StockRow>();

            Price grandTotal = null;
            String mixedCurrencies = null;

            for (Product p : products) {
                Quantity qty = stockLevels.get(p.getId());
                if (qty == null) {
                    rows.add(new StockRow(
                        p.getCode().toString(), p.getName(),
                        "—", p.getPrice().toString(), "—"
                    ));
                    continue;
                }

                Price lineTotal = p.getPrice().multiply(qty.value());

                if (mixedCurrencies == null) {
                    try {
                        grandTotal = grandTotal == null ? lineTotal : grandTotal.add(lineTotal);
                    } catch (IllegalArgumentException e) {
                        mixedCurrencies = e.getMessage();
                        grandTotal = null;
                    }
                }

                rows.add(new StockRow(
                    p.getCode().toString(), p.getName(),
                    qty.toString(), p.getPrice().toString(), lineTotal.toString()
                ));
            }

            table.setItems(FXCollections.observableArrayList(rows));

            if (mixedCurrencies != null) {
                totalLabel.setText("Итого: невозможно подсчитать — " + mixedCurrencies);
            } else if (grandTotal != null) {
                totalLabel.setText("Итого: " + grandTotal);
            } else {
                totalLabel.setText("");
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public Tab getTab() { return tab; }
}
