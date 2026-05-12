package com.warehouse.domain.entity;

import com.warehouse.domain.valueobject.Price;
import com.warehouse.domain.valueobject.ProductCode;
import com.warehouse.domain.valueobject.Weight;

public class Product {

    private Long id;
    private String name;
    private ProductCode code;
    private Price price;
    private Weight weight;
    private String description;

    public Product(Long id, String name, ProductCode code, Price price, Weight weight, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.weight = weight;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public ProductCode getCode() { return code; }
    public Price getPrice() { return price; }
    public Weight getWeight() { return weight; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setCode(ProductCode code) { this.code = code; }
    public void setPrice(Price price) { this.price = price; }
    public void setWeight(Weight weight) { this.weight = weight; }
    public void setDescription(String description) { this.description = description; }
}
