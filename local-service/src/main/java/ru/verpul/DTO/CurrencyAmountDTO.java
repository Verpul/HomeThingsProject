package ru.verpul.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyAmountDTO {
    private BigDecimal amount = new BigDecimal(0);
    private BigDecimal sellPrice = new BigDecimal(0);
    private BigDecimal rublesSpentOn = new BigDecimal(0);
    private BigDecimal rublesEarnedFrom = new BigDecimal(0);
    private BigDecimal difference = new BigDecimal(0);
}
