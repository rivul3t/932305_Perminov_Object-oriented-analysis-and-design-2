package com.warehouse.domain.valueobject;

public record ProductCode(String value) {

    public ProductCode {
        if (value == null || !value.matches("[A-Z]{3}-\\d{4}"))
            throw new IllegalArgumentException(
                "Код товара должен быть в формате ABC-1234 (3 буквы, дефис, 4 цифры)"
            );
    }

    @Override
    public String toString() {
        return value;
    }
}
