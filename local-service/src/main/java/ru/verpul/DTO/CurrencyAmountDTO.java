package ru.verpul.DTO;

import lombok.Data;

@Data
public class CurrencyAmountDTO {
    private double amount;
    private double sellPrice;
    private double rublesSpentOn;
    private double rublesEarnedFrom;
    private double difference;
}
