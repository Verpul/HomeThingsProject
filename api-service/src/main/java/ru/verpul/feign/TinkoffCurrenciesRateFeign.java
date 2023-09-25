package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "tinkoff-currencies-rate", url = "https://api.tinkoff.ru/v1/currency_rates")
public interface TinkoffCurrenciesRateFeign {
    @GetMapping
    ResponseEntity<String> getTinkoffCurrenciesRate();
}
