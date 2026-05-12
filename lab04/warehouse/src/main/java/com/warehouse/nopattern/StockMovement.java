package com.warehouse.nopattern;

import java.time.LocalDateTime;

public class StockMovement {
    private Long id;
    private Long productId;
    private String type;

    private int qtyValue;
    private String qtyUnit;

    private LocalDateTime movedAt;
    private String comment;

    public StockMovement(Long id, Long productId, String type,
                         int qtyValue, String qtyUnit,
                         LocalDateTime movedAt, String comment) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.qtyValue = qtyValue;
        this.qtyUnit = qtyUnit;
        this.movedAt = movedAt;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getType() { return type; }
    public int getQtyValue() { return qtyValue; }
    public String getQtyUnit() { return qtyUnit; }
    public LocalDateTime getMovedAt() { return movedAt; }
    public String getComment() { return comment; }
}
