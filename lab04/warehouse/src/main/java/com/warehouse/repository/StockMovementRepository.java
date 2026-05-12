package com.warehouse.repository;

import com.warehouse.domain.entity.StockMovement;
import com.warehouse.domain.valueobject.Quantity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementRepository {

    public StockMovement save(StockMovement movement) throws SQLException {
        var sql = """
            INSERT INTO stock_movements (product_id, type, qty_value, qty_unit, moved_at, comment)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (var ps = Database.getConnection().prepareStatement(sql,
                java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, movement.getProductId());
            ps.setString(2, movement.getType().name());
            ps.setInt(3, movement.getQuantity().value());
            ps.setString(4, movement.getQuantity().unit());
            ps.setString(5, movement.getMovedAt().toString());
            ps.setString(6, movement.getComment());
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                keys.next();
                return new StockMovement(keys.getLong(1), movement.getProductId(),
                        movement.getType(), movement.getQuantity(),
                        movement.getMovedAt(), movement.getComment());
            }
        }
    }

    public List<StockMovement> findByProductId(long productId) throws SQLException {
        var sql = "SELECT * FROM stock_movements WHERE product_id = ? ORDER BY moved_at DESC";
        var list = new ArrayList<StockMovement>();
        try (var ps = Database.getConnection().prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<StockMovement> findAll() throws SQLException {
        var sql = """
            SELECT sm.*, p.name as product_name
            FROM stock_movements sm
            JOIN products p ON p.id = sm.product_id
            ORDER BY sm.moved_at DESC
            """;
        var list = new ArrayList<StockMovement>();
        try (var stmt = Database.getConnection().createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private StockMovement mapRow(ResultSet rs) throws SQLException {
        return new StockMovement(
            rs.getLong("id"),
            rs.getLong("product_id"),
            StockMovement.Type.valueOf(rs.getString("type")),
            new Quantity(rs.getInt("qty_value"), rs.getString("qty_unit")),
            LocalDateTime.parse(rs.getString("moved_at").replace(" ", "T")),
            rs.getString("comment")
        );
    }
}
