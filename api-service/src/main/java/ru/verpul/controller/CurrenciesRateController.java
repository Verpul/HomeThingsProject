package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verpul.DTO.CBCurrencyRateDTO;
import ru.verpul.DTO.TinkoffCurrencyRateDTO;
import ru.verpul.service.CurrenciesRateService;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CurrenciesRateController {
    private final CurrenciesRateService currenciesRateService;

    @GetMapping("/cb")
    public CBCurrencyRateDTO getCBCurrencyRates() {
        return currenciesRateService.loadCBCurrenciesRate();
    }

    @GetMapping("/tinkoff")
    public TinkoffCurrencyRateDTO getTinkoffCurrencyRates() {
        return currenciesRateService.loadTinkoffCurrenciesRate();
    }
}
