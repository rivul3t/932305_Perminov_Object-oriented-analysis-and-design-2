package com.warehouse.repository;

import com.warehouse.domain.entity.Product;
import com.warehouse.domain.valueobject.Currency;
import com.warehouse.domain.valueobject.Price;
import com.warehouse.domain.valueobject.ProductCode;
import com.warehouse.domain.valueobject.Weight;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {

    public List<Product> findAll() throws SQLException {
        var sql = "SELECT * FROM products ORDER BY name";
        var list = new ArrayList<Product>();
        try (var stmt = Database.getConnection().createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Optional<Product> findById(long id) throws SQLException {
        var sql = "SELECT * FROM products WHERE id = ?";
        try (var ps = Database.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public List<Product> search(String query) throws SQLException {
        var sql = "SELECT * FROM products WHERE lower(name) LIKE ? OR code LIKE ? ORDER BY name";
        var list = new ArrayList<Product>();
        try (var ps = Database.getConnection().prepareStatement(sql)) {
            String pattern = "%" + query.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern.toUpperCase());
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Product save(Product product) throws SQLException {
        if (product.getId() == null) {
            return insert(product);
        } else {
            return update(product);
        }
    }

    private Product insert(Product p) throws SQLException {
        var sql = """
            INSERT INTO products (name, code, price_amount, price_currency, weight_value, weight_unit, description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (var ps = Database.getConnection().prepareStatement(sql,
                java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCode().value());
            ps.setBigDecimal(3, p.getPrice().amount());
            ps.setString(4, p.getPrice().currency().code());
            ps.setBigDecimal(5, p.getWeight().value());
            ps.setString(6, p.getWeight().unit());
            ps.setString(7, p.getDescription());
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Product(keys.getLong(1), p.getName(), p.getCode(),
                        p.getPrice(), p.getWeight(), p.getDescription());
            }
        }
    }

    private Product update(Product p) throws SQLException {
        var sql = """
            UPDATE products SET name=?, code=?, price_amount=?, price_currency=?,
            weight_value=?, weight_unit=?, description=? WHERE id=?
            """;
        try (var ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCode().value());
            ps.setBigDecimal(3, p.getPrice().amount());
            ps.setString(4, p.getPrice().currency().code());
            ps.setBigDecimal(5, p.getWeight().value());
            ps.setString(6, p.getWeight().unit());
            ps.setString(7, p.getDescription());
            ps.setLong(8, p.getId());
            ps.executeUpdate();
        }
        return p;
    }

    public void delete(long id) throws SQLException {
        try (var ps = Database.getConnection().prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getLong("id"),
            rs.getString("name"),
            new ProductCode(rs.getString("code")),
            new Price(
                rs.getBigDecimal("price_amount"),
                new Currency(rs.getString("price_currency"))
            ),
            new Weight(
                rs.getBigDecimal("weight_value"),
                rs.getString("weight_unit")
            ),
            rs.getString("description")
        );
    }
}
