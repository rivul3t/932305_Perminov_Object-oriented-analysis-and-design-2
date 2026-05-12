package com.warehouse.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Price(BigDecimal amount, Currency currency) {

    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        if (currency == null)
            throw new IllegalArgumentException("Валюта обязательна");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Price add(Price other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException(
                "Нельзя складывать цены в разных валютах: %s и %s".formatted(this.currency, other.currency)
            );
        return new Price(this.amount.add(other.amount), this.currency);
    }

    public Price multiply(int factor) {
        if (factor < 0)
            throw new IllegalArgumentException("Множитель не может быть отрицательным");
        return new Price(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(amount.toPlainString(), currency);
    }
}
