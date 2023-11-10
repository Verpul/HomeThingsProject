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
        return currencyRepository.findAll().stream()
                .map(currencyMapper::currencyToCurrencyDTO)
                .collect(Collectors.toList());
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
        List<Currency> allCurrencyRecords = currencyRepository.findAll();

        for (Currency currencyRecord : allCurrencyRecords) {
            CurrencyAmountDTO recordToChange = currencyAmountMap.get(currencyRecord.getCurrencyFrom());
            recordToChange.setAmount(recordToChange.getAmount() - currencyRecord.getCurrencyFromAmount());

            if (currencyRecord.getCurrencyTo() == CurrencyType.RUB)
                recordToChange.setRublesEarnedFrom(recordToChange.getRublesEarnedFrom() + currencyRecord.getCurrencyToAmount());

            recordToChange = currencyAmountMap.get(currencyRecord.getCurrencyTo());
            recordToChange.setAmount(recordToChange.getAmount() + currencyRecord.getCurrencyToAmount());

            if (currencyRecord.getCurrencyFrom() == CurrencyType.RUB)
                recordToChange.setRublesSpentOn(recordToChange.getRublesSpentOn() + currencyRecord.getCurrencyFromAmount());
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

//    public CurrencyAmountDTO calculateCurrency() {
//        List<Currency> allCurrencyRecords = currencyRepository.findAll();
//        CurrencyAmountDTO currencyAmount = new CurrencyAmountDTO();
//
//        for (CurrencyType type: CurrencyType.values()) {
//            countCurrencyAmount();
//        }
//
//        return currencyAmount;
//    }
//
//    private void countCurrencyAmount(CurrencyType type, CurrencyAmountDTO currencyAmount, List<Currency> currencyRecords) {
//        double amount = currencyRecords.stream()
//                .filter(record -> record.getCurrencyTo() == type || record.getCurrencyFrom() == type)
//                .mapToDouble(record -> record.getCurrencyTo() == type ? record.getCurrencyToAmount() : -record.getCurrencyFromAmount())
//                .sum();
//
//        Map<String, Double> currencyOverallMap = currencyAmount.getCurrencyOverall();
//        currencyOverallMap.put(type.toString(), amount);
//    }
//
//    private void countRublesSpentOnCurrency(CurrencyType type, CurrencyAmountDTO currencyAmount, List<Currency> currencyRecords) {
//        double amount = currencyRecords.stream()
//                .filter(record -> record.getCurrencyFrom() == CurrencyType.RUB && record.getCurrencyTo() == type)
//                .mapToDouble(Currency::getCurrencyFromAmount)
//                .sum();
//
//        Map<String, Double> rublesSpent = currencyAmount.getRublesSpentOnCurrency();
//        rublesSpent.put(type.toString(), amount);
//    }
//
//    private void countRublesEarnedFromCurrency(CurrencyType type, CurrencyAmountDTO currencyAmount, List<Currency> currencyRecords) {
//        double amount = currencyRecords.stream()
//                .filter(record -> record.getCurrencyFrom() == type && record.getCurrencyTo() == CurrencyType.RUB)
//                .mapToDouble(Currency::getCurrencyFromAmount)
//                .sum();
//
//        Map<String, Double> rublesEarned = currencyAmount.getRublesEarnedFromCurrency();
//        rublesEarned.put(type.toString(), amount);
//    }
//
//    private void countSellPrice(CurrencyType type) {
//
//    }
}
