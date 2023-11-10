package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.CurrencyAmountDTO;
import ru.verpul.DTO.CurrencyDTO;
import ru.verpul.DTO.TinkoffCurrencyRateDTO;
import ru.verpul.enums.CurrencyType;
import ru.verpul.exception.NotFoundException;
import ru.verpul.feign.ApiServiceFeign;
import ru.verpul.mapper.CurrencyMapper;
import ru.verpul.model.Currency;
import ru.verpul.repository.CurrencyRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;
    private final ApiServiceFeign apiServiceFeign;

    public void saveCurrencyRecord(CurrencyDTO currencyDTO) {
        currencyRepository.save(currencyMapper.currencyDTOtoCurrency(currencyDTO));
    }

    public List<CurrencyDTO> getCurrencyRecords() {
        List<CurrencyDTO> currencyRecords = currencyRepository.findAllAndSortByDate().stream()
                .map(currencyMapper::currencyToCurrencyDTO)
                .collect(Collectors.toList());
        Collections.reverse(currencyRecords);

        return currencyRecords;
    }

    public void updateCurrencyRecord(CurrencyDTO currencyDTO, Long id) {
        currencyRepository.findById(id)
                .map(foundCurrencyRecord -> {
                    foundCurrencyRecord.setCurrencyFrom(currencyDTO.getCurrencyFrom());
                    foundCurrencyRecord.setCurrencyFromAmount(currencyDTO.getCurrencyFromAmount());
                    foundCurrencyRecord.setCurrencyTo(currencyDTO.getCurrencyTo());
                    foundCurrencyRecord.setCurrencyToAmount(currencyDTO.getCurrencyToAmount());
                    foundCurrencyRecord.setRate(currencyDTO.getRate());
                    foundCurrencyRecord.setExchangeDate(currencyDTO.getExchangeDate());

                    return currencyRepository.save(foundCurrencyRecord);
                }).orElseThrow(() -> new NotFoundException("Запись с id = " + id + " не найдена"));
    }

    public void deleteCurrencyRecord(Long id) {
        currencyRepository.findById(id).ifPresent(currencyRepository::delete);
    }

    public Map<CurrencyType, CurrencyAmountDTO> calculateCurrency() {
        HashMap<CurrencyType, CurrencyAmountDTO> currencyAmountMap = new HashMap<>();

        for (CurrencyType type: CurrencyType.values()) {
            currencyAmountMap.put(type, new CurrencyAmountDTO());
        }

        calculateCurrencyAmountAndRublesSpentAndEarned(currencyAmountMap);
        calculateSellPriceAndDifference(currencyAmountMap);

        return currencyAmountMap;
    }

    private void calculateCurrencyAmountAndRublesSpentAndEarned(Map<CurrencyType, CurrencyAmountDTO> currencyAmountMap) {
        List<Currency> allCurrencyRecords = currencyRepository.findAllAndSortByDate();

        for (Currency currencyRecord : allCurrencyRecords) {
            CurrencyAmountDTO recordToChangeFrom = currencyAmountMap.get(currencyRecord.getCurrencyFrom());
            CurrencyAmountDTO recordToChangeTo = currencyAmountMap.get(currencyRecord.getCurrencyTo());

            if (currencyRecord.getCurrencyFrom() != CurrencyType.RUB && currencyRecord.getCurrencyTo() != CurrencyType.RUB) {
                double averageRate = recordToChangeFrom.getRublesSpentOn() / recordToChangeFrom.getAmount();
                double sumToExchange = currencyRecord.getCurrencyFromAmount() * averageRate;

                recordToChangeFrom.setRublesSpentOn(recordToChangeFrom.getRublesSpentOn() - sumToExchange);
                recordToChangeTo.setRublesSpentOn(recordToChangeTo.getRublesSpentOn() + sumToExchange);
            }

            recordToChangeFrom.setAmount(recordToChangeFrom.getAmount() - currencyRecord.getCurrencyFromAmount());
            recordToChangeTo.setAmount(recordToChangeTo.getAmount() + currencyRecord.getCurrencyToAmount());

            if (currencyRecord.getCurrencyTo() == CurrencyType.RUB)
                recordToChangeFrom.setRublesEarnedFrom(recordToChangeFrom.getRublesEarnedFrom() + currencyRecord.getCurrencyToAmount());

            if (currencyRecord.getCurrencyFrom() == CurrencyType.RUB)
                recordToChangeTo.setRublesSpentOn(recordToChangeTo.getRublesSpentOn() + currencyRecord.getCurrencyFromAmount());
        }
    }

    private void calculateSellPriceAndDifference(Map<CurrencyType, CurrencyAmountDTO> currencyAmountMap) {
        TinkoffCurrencyRateDTO tinkoffCurrencyRateDTO = apiServiceFeign.getTinkoffCurrencyRates();

        for (CurrencyType type : tinkoffCurrencyRateDTO.getCurrenciesRate().keySet()) {
            String sellPrice = tinkoffCurrencyRateDTO.getCurrenciesRate().get(type).get("sell");

            CurrencyAmountDTO currencyAmountToChange = currencyAmountMap.get(type);
            currencyAmountToChange.setSellPrice(Double.parseDouble(sellPrice) * currencyAmountToChange.getAmount());
            currencyAmountToChange.setDifference(currencyAmountToChange.getSellPrice() - currencyAmountToChange.getRublesSpentOn());
        }
    }
}
