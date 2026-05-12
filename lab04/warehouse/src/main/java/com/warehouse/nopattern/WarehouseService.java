package com.warehouse.nopattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WarehouseService {


    public void addProduct(String name, String code,
                           BigDecimal price, String currency,
                           BigDecimal weightValue, String weightUnit) {

        if (code == null || !code.matches("[A-Z]{3}-\\d{4}"))
            throw new IllegalArgumentException("Неверный формат кода: " + code);

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Цена не может быть отрицательной");

        if (currency == null || !currency.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("Неверный код валюты: " + currency);

        if (weightValue == null || weightValue.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Вес должен быть больше нуля");

        if (weightUnit == null || (!weightUnit.equals("кг") && !weightUnit.equals("г")))
            throw new IllegalArgumentException("Неверная единица веса: " + weightUnit);

    }

    public void updateProduct(Long id, String name, String code,
                              BigDecimal price, String currency,
                              BigDecimal weightValue, String weightUnit) {

        if (code == null || !code.matches("[A-Z]{3}-\\d{4}"))
            throw new IllegalArgumentException("Неверный формат кода: " + code);

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Цена не может быть отрицательной");

        if (currency == null || !currency.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("Неверный код валюты: " + currency);

        if (weightValue == null || weightValue.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Вес должен быть больше нуля");

        if (weightUnit == null || (!weightUnit.equals("кг") && !weightUnit.equals("г")))
            throw new IllegalArgumentException("Неверная единица веса: " + weightUnit);

    }

    public BigDecimal calculateLineTotal(BigDecimal price, int qty) {
        return price.multiply(BigDecimal.valueOf(qty));
    }

    public BigDecimal calculateGrandTotal(List<Product> products, List<Integer> quantities) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < products.size(); i++) {
            total = total.add(products.get(i).getPrice()
                    .multiply(BigDecimal.valueOf(quantities.get(i))));
        }
        return total;
    }

    public void receiveStock(Long productId, int qty, String unit, String comment) {
        if (qty <= 0)
            throw new IllegalArgumentException("Количество должно быть больше нуля");

        if (unit == null || (!unit.equals("шт") && !unit.equals("кг") && !unit.equals("л")))
            throw new IllegalArgumentException("Неверная единица: " + unit);

    }

    public void writeOffStock(Long productId, int qty, String unit, String comment) {
        if (qty <= 0)
            throw new IllegalArgumentException("Количество должно быть больше нуля");

        if (unit == null || (!unit.equals("шт") && !unit.equals("кг") && !unit.equals("л")))
            throw new IllegalArgumentException("Неверная единица: " + unit);

        int currentStock = getCurrentStock(productId, unit);
        if (currentStock < qty)
            throw new IllegalStateException("Недостаточно товара на складе");

    }

    public List<StockMovement> getMovementsInPeriod(Long productId,
                                                     LocalDate from, LocalDate to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("Даты обязательны");
        if (from.isAfter(to))
            throw new IllegalArgumentException("Дата начала не может быть позже даты конца");

        return List.of();
    }

    public List<StockMovement> getMovementsReport(LocalDate from, LocalDate to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("Даты обязательны");
        if (from.isAfter(to))
            throw new IllegalArgumentException("Дата начала не может быть позже даты конца");

        return List.of();
    }

    private int getCurrentStock(Long productId, String unit) {
        return 0;
    }
}
