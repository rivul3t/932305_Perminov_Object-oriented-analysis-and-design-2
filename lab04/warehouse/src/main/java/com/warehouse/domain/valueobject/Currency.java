package com.warehouse.domain.valueobject;

public record Currency(String code) {

    public Currency {
        if (code == null || !code.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("Код валюты должен состоять из 3 заглавных букв, например: RUB, USD");
    }

    @Override
    public String toString() {
        return code;
    }
}
