package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cb-currencies-rate", url = "https://www.cbr-xml-daily.ru/daily_json.js")
public interface CBCurrenciesRateFeign {
    @GetMapping
    ResponseEntity<String> getCBCurrenciesRate();
}
