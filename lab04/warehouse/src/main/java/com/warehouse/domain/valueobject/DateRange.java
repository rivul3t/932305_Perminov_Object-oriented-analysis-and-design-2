package com.warehouse.domain.valueobject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DateRange(LocalDate from, LocalDate to) {

    public DateRange {
        if (from == null || to == null)
            throw new IllegalArgumentException("Даты начала и конца обязательны");
        if (from.isAfter(to))
            throw new IllegalArgumentException("Дата начала не может быть позже даты конца");
    }

    public boolean contains(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        return !date.isBefore(from) && !date.isAfter(to);
    }

    @Override
    public String toString() {
        return "%s — %s".formatted(from, to);
    }
}
