package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.verpul.DTO.TinkoffCurrencyRateDTO;

@FeignClient("api-service")
public interface ApiServiceFeign {

    @GetMapping("/api/rates/tinkoff")
    TinkoffCurrencyRateDTO getTinkoffCurrencyRates();

}
