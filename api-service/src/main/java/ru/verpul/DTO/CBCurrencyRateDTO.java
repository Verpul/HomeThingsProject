package ru.verpul.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CBCurrencyRateDTO {
    private String currenciesLastUpdateTime;

    private Map<String, String> currenciesRate = new HashMap<>();
}
