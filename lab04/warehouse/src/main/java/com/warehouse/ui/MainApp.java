package com.warehouse.ui;

import com.warehouse.repository.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Database.init();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка инициализации БД: " + e.getMessage()).showAndWait();
            return;
        }

        var tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        var productsTab = new ProductsTab();
        var movementsTab = new MovementsTab(productsTab::refresh);
        var stockTab = new StockTab();

        tabPane.getTabs().addAll(
            productsTab.getTab(),
            movementsTab.getTab(),
            stockTab.getTab()
        );

        // Обновлять вкладку остатков при переключении на неё
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == stockTab.getTab()) stockTab.refresh();
        });

        var scene = new Scene(tabPane, 900, 600);
        stage.setTitle("Склад");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
