package ru.verpul.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.verpul.DTO.CBCurrencyRateDTO;
import ru.verpul.DTO.TinkoffCurrencyRateDTO;
import ru.verpul.enums.Currency;
import ru.verpul.feign.CBCurrenciesRateFeign;
import ru.verpul.feign.TinkoffCurrenciesRateFeign;
import ru.verpul.util.CommonUtil;
import ru.verpul.util.CurrenciesRateUtil;

import javax.annotation.PostConstruct;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrenciesRateService {
    private static CBCurrencyRateDTO lastCBCurrenciesRateData;

    private final CBCurrenciesRateFeign cbCurrenciesRateFeign;
    private final TinkoffCurrenciesRateFeign tinkoffCurrenciesRateFeign;

    public CBCurrencyRateDTO loadCBCurrenciesRate() {
        if (lastCBCurrenciesRateData == null || CurrenciesRateUtil.isDateTimeSuitableForUpdate()) {
            try {
                ResponseEntity<String> response = cbCurrenciesRateFeign.getCBCurrenciesRate();
                if (response.getStatusCodeValue() == 200) {
                    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

                    String currenciesUpdateTime = CommonUtil.getFormattedUpdateDate(jsonNode.get("Date").asText());
                    Map<String, String> currenciesRate = new HashMap<>();

                    for (Currency currency : Currency.values()) {
                        String currencyValue = jsonNode.get("Valute").get(currency.toString()).get("Value").asText();
                        currenciesRate.put(currency.toString(), currencyValue);
                    }

                    CBCurrencyRateDTO cbCurrencyRateDTO = new CBCurrencyRateDTO();
                    cbCurrencyRateDTO.setCurrenciesLastUpdateTime(currenciesUpdateTime);
                    cbCurrencyRateDTO.setCurrenciesRate(currenciesRate);

                    lastCBCurrenciesRateData = cbCurrencyRateDTO;
                    return lastCBCurrenciesRateData;
                }
            } catch (FeignException | JsonProcessingException e) {
                log.error("Error while loading Central Bank currency rates", e);
            }
        }
        return lastCBCurrenciesRateData;
    }


    public TinkoffCurrencyRateDTO loadTinkoffCurrenciesRate() {
        try {
            ResponseEntity<String> response = tinkoffCurrenciesRateFeign.getTinkoffCurrenciesRate();
            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

            String lastUpdate = CurrenciesRateUtil.convertMillisToDateTime(jsonNode.get("payload").get("lastUpdate").get("milliseconds").asLong());
            Map<String, Map<String, String>> currenciesRate = new HashMap<>();
            for (JsonNode node : jsonNode.get("payload").get("rates")) {
                if (node.get("category").asText().equals("CreditCardsTransfers")
                        && node.get("toCurrency").get("name").asText().equals("RUB")
                        && ObjectUtils.containsConstant(Currency.values(), node.get("fromCurrency").get("name").asText())) {
                    Map<String, String> currencyRates = new HashMap<>();
                    currencyRates.put("buy", node.get("buy").asText());
                    currencyRates.put("sell", node.get("sell").asText());

                    String currency = node.get("fromCurrency").get("name").asText();
                    currenciesRate.put(currency, currencyRates);
                }
            }
            TinkoffCurrencyRateDTO tinkoffCurrencyRateDTO = new TinkoffCurrencyRateDTO();
            tinkoffCurrencyRateDTO.setCurrenciesRate(currenciesRate);
            tinkoffCurrencyRateDTO.setCurrenciesLastUpdateTime(lastUpdate);

            return tinkoffCurrencyRateDTO;
        } catch (FeignException | JsonProcessingException e) {
            log.error("Error while loading Tinkoff Bank currency rates", e);
        }
        return null;
    }
}
