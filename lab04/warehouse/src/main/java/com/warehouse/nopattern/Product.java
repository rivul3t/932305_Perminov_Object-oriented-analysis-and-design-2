package com.warehouse.nopattern;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;

    private String code;

    private BigDecimal price;
    private String currency;

    private BigDecimal weightValue;
    private String weightUnit;

    private String description;

    public Product(Long id, String name, String code,
                   BigDecimal price, String currency,
                   BigDecimal weightValue, String weightUnit,
                   String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.currency = currency;
        this.weightValue = weightValue;
        this.weightUnit = weightUnit;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public BigDecimal getWeightValue() { return weightValue; }
    public String getWeightUnit() { return weightUnit; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setWeightValue(BigDecimal weightValue) { this.weightValue = weightValue; }
    public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    public void setDescription(String description) { this.description = description; }
}
