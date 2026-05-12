package com.warehouse.domain.entity;

import com.warehouse.domain.valueobject.Quantity;

import java.time.LocalDateTime;

public class StockMovement {

    public enum Type { IN, OUT }

    private Long id;
    private Long productId;
    private Type type;
    private Quantity quantity;
    private LocalDateTime movedAt;
    private String comment;

    public StockMovement(Long id, Long productId, Type type, Quantity quantity,
                         LocalDateTime movedAt, String comment) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.movedAt = movedAt;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public Type getType() { return type; }
    public Quantity getQuantity() { return quantity; }
    public LocalDateTime getMovedAt() { return movedAt; }
    public String getComment() { return comment; }
}
