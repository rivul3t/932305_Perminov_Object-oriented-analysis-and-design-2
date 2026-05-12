package com.warehouse.service;

import com.warehouse.domain.entity.Product;
import com.warehouse.domain.entity.StockMovement;
import com.warehouse.domain.valueobject.Quantity;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.StockMovementRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WarehouseService {

    private final ProductRepository productRepo = new ProductRepository();
    private final StockMovementRepository movementRepo = new StockMovementRepository();

    public List<Product> getAllProducts() throws SQLException {
        return productRepo.findAll();
    }

    public List<Product> searchProducts(String query) throws SQLException {
        return productRepo.search(query);
    }

    public Optional<Product> getProduct(long id) throws SQLException {
        return productRepo.findById(id);
    }

    public Product saveProduct(Product product) throws SQLException {
        return productRepo.save(product);
    }

    public void deleteProduct(long id) throws SQLException {
        productRepo.delete(id);
    }

    public void receiveStock(long productId, Quantity quantity, String comment) throws SQLException {
        var movement = new StockMovement(null, productId, StockMovement.Type.IN,
                quantity, LocalDateTime.now(), comment);
        movementRepo.save(movement);
    }

    public void writeOffStock(long productId, Quantity quantity, String comment) throws SQLException {
        Quantity current = getCurrentStock(productId);
        if (current == null || current.value() < quantity.value())
            throw new IllegalStateException("Недостаточно товара на складе");
        var movement = new StockMovement(null, productId, StockMovement.Type.OUT,
                quantity, LocalDateTime.now(), comment);
        movementRepo.save(movement);
    }

    public Quantity getCurrentStock(long productId) throws SQLException {
        var movements = movementRepo.findByProductId(productId);
        if (movements.isEmpty()) return null;

        String unit = movements.getFirst().getQuantity().unit();
        int total = 0;
        for (var m : movements) {
            if (m.getType() == StockMovement.Type.IN) {
                total += m.getQuantity().value();
            } else {
                total -= m.getQuantity().value();
            }
        }
        return new Quantity(total, unit);
    }

    public Map<Long, Quantity> getAllStockLevels() throws SQLException {
        var movements = movementRepo.findAll();
        Map<Long, int[]> totals = new HashMap<>();
        Map<Long, String> units = new HashMap<>();

        for (var m : movements) {
            long pid = m.getProductId();
            totals.putIfAbsent(pid, new int[]{0});
            units.putIfAbsent(pid, m.getQuantity().unit());
            if (m.getType() == StockMovement.Type.IN) {
                totals.get(pid)[0] += m.getQuantity().value();
            } else {
                totals.get(pid)[0] -= m.getQuantity().value();
            }
        }

        Map<Long, Quantity> result = new HashMap<>();
        totals.forEach((pid, arr) ->
            result.put(pid, new Quantity(Math.max(0, arr[0]), units.get(pid)))
        );
        return result;
    }

    public List<StockMovement> getAllMovements() throws SQLException {
        return movementRepo.findAll();
    }
}
