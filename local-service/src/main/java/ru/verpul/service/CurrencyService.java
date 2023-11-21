package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

        for (CurrencyType type : CurrencyType.values()) {
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

            BigDecimal currencyFromAmount = BigDecimal.valueOf(currencyRecord.getCurrencyFromAmount());
            BigDecimal currencyToAmount = BigDecimal.valueOf(currencyRecord.getCurrencyToAmount());

            if (currencyRecord.getCurrencyFrom() != CurrencyType.RUB && currencyRecord.getCurrencyTo() != CurrencyType.RUB) {
                BigDecimal averageRate = recordToChangeFrom.getRublesSpentOn()
                        .divide(recordToChangeFrom.getAmount(), RoundingMode.HALF_UP);
                BigDecimal sumToExchange = currencyFromAmount.multiply(averageRate);

                recordToChangeFrom.setRublesSpentOn(recordToChangeFrom.getRublesSpentOn()
                        .subtract(sumToExchange)
                        .setScale(2, RoundingMode.HALF_UP));

                recordToChangeTo.setRublesSpentOn(recordToChangeTo.getRublesSpentOn()
                        .add(sumToExchange)
                        .setScale(2, RoundingMode.HALF_UP));
            }

            recordToChangeFrom.setAmount(recordToChangeFrom.getAmount()
                    .subtract(currencyFromAmount)
                    .setScale(2, RoundingMode.HALF_UP));

            recordToChangeTo.setAmount(recordToChangeTo.getAmount()
                    .add(currencyToAmount)
                    .setScale(2, RoundingMode.HALF_UP));

            if (currencyRecord.getCurrencyTo() == CurrencyType.RUB)
                recordToChangeFrom.setRublesEarnedFrom(recordToChangeFrom.getRublesEarnedFrom()
                        .add(currencyToAmount)
                        .setScale(2, RoundingMode.HALF_UP));

            if (currencyRecord.getCurrencyFrom() == CurrencyType.RUB)
                recordToChangeTo.setRublesSpentOn(recordToChangeTo.getRublesSpentOn()
                        .add(currencyFromAmount)
                        .setScale(2, RoundingMode.HALF_UP));
        }
    }

    private void calculateSellPriceAndDifference(Map<CurrencyType, CurrencyAmountDTO> currencyAmountMap) {
        TinkoffCurrencyRateDTO tinkoffCurrencyRateDTO = apiServiceFeign.getTinkoffCurrencyRates();

        for (CurrencyType type : tinkoffCurrencyRateDTO.getCurrenciesRate().keySet()) {
            double bankBuyPrice = Double.parseDouble(tinkoffCurrencyRateDTO.getCurrenciesRate().get(type).get("buy"));
            CurrencyAmountDTO currencyAmountToChange = currencyAmountMap.get(type);

            currencyAmountToChange.setSellPrice(BigDecimal.valueOf(bankBuyPrice)
                    .multiply(currencyAmountToChange.getAmount())
                    .setScale(2, RoundingMode.HALF_UP));

            currencyAmountToChange.setDifference(currencyAmountToChange.getSellPrice()
                    .subtract(currencyAmountToChange.getRublesSpentOn())
                    .setScale(2, RoundingMode.HALF_UP));
        }
    }

    public ByteArrayOutputStream getXLSFile() {
        List<Currency> currencyList = currencyRepository.findAllAndSortByDate();
        List<String> headers = List.of("Дата обмена", "Валюта из", "Количество", "Валюта в", "Количество", "Курс");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Currency data");

            for (int i = 0; i < headers.size(); i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }

            CreationHelper creationHelper = workbook.getCreationHelper();
            short dateFormat = creationHelper.createDataFormat().getFormat("dd.mm.yyyy");
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(dateFormat);
            setCellBorderStyle(dateStyle, BorderStyle.THIN);

            CellStyle headerStyle = workbook.createCellStyle();
            setCellBorderStyle(headerStyle, BorderStyle.MEDIUM);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle borderStyle = workbook.createCellStyle();
            setCellBorderStyle(borderStyle, BorderStyle.THIN);

            int rowNum = 0;
            int cellNum = 0;
            Row row = sheet.createRow(rowNum++);

            for (String header : headers) {
                createCellAndApplyStyle(row, cellNum++, headerStyle).setCellValue(header);
            }

            for (Currency record : currencyList) {
                row = sheet.createRow(rowNum++);
                cellNum = 0;

                createCellAndApplyStyle(row, cellNum++, dateStyle).setCellValue(record.getExchangeDate());
                createCellAndApplyStyle(row, cellNum++, borderStyle).setCellValue(record.getCurrencyFrom().toString());
                createCellAndApplyStyle(row, cellNum++, borderStyle).setCellValue(record.getCurrencyFromAmount());
                createCellAndApplyStyle(row, cellNum++, borderStyle).setCellValue(record.getCurrencyTo().toString());
                createCellAndApplyStyle(row, cellNum++, borderStyle).setCellValue(record.getCurrencyToAmount());
                createCellAndApplyStyle(row, cellNum, borderStyle).setCellValue(record.getRate());
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            log.error("Ошибка при формировании xls файла", e);
        }

        return null;
    }

    private Cell createCellAndApplyStyle(Row row, int cellNum, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNum);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private void setCellBorderStyle(CellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
    }
}
