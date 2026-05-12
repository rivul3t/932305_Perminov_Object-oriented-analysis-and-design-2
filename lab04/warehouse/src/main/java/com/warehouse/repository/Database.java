package com.warehouse.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:sqlite:warehouse.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public static void init() throws SQLException {
        String createProducts = """
            CREATE TABLE IF NOT EXISTS products (
                id               INTEGER PRIMARY KEY AUTOINCREMENT,
                name             TEXT    NOT NULL,
                code             TEXT    NOT NULL UNIQUE,
                price_amount     DECIMAL NOT NULL,
                price_currency   TEXT    NOT NULL,
                weight_value     DECIMAL NOT NULL,
                weight_unit      TEXT    NOT NULL,
                description      TEXT
            )
            """;

        String createMovements = """
            CREATE TABLE IF NOT EXISTS stock_movements (
                id          INTEGER  PRIMARY KEY AUTOINCREMENT,
                product_id  INTEGER  NOT NULL,
                type        TEXT     NOT NULL,
                qty_value   INTEGER  NOT NULL,
                qty_unit    TEXT     NOT NULL,
                moved_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
                comment     TEXT,
                FOREIGN KEY (product_id) REFERENCES products(id)
            )
            """;

        try (var stmt = getConnection().createStatement()) {
            stmt.execute(createProducts);
            stmt.execute(createMovements);
        }
    }
}
