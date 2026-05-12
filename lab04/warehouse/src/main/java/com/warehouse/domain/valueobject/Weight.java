package com.warehouse.domain.valueobject;

import java.math.BigDecimal;
import java.util.Set;

public record Weight(BigDecimal value, String unit) {

    private static final Set<String> ALLOWED_UNITS = Set.of("кг", "г");

    public Weight {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Вес должен быть больше нуля");
        if (unit == null || !ALLOWED_UNITS.contains(unit))
            throw new IllegalArgumentException("Единица веса должна быть одной из: " + ALLOWED_UNITS);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(value.toPlainString(), unit);
    }
}
