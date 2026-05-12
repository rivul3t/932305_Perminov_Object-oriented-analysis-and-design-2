package com.warehouse.domain.valueobject;

import java.util.Set;

public record Quantity(int value, String unit) {

    private static final Set<String> ALLOWED_UNITS = Set.of("шт", "кг", "л");

    public Quantity {
        if (value < 0)
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        if (unit == null || !ALLOWED_UNITS.contains(unit))
            throw new IllegalArgumentException("Единица измерения должна быть одной из: " + ALLOWED_UNITS);
    }

    @Override
    public String toString() {
        return "%d %s".formatted(value, unit);
    }
}
