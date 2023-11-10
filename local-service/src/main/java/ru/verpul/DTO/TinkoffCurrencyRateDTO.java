package ru.verpul.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.verpul.enums.CurrencyType;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TinkoffCurrencyRateDTO {
    private String currenciesLastUpdateTime;
    private Map<CurrencyType, Map<String, String>> currenciesRate = new HashMap<>();
}
