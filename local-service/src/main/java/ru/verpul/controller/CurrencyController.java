package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.CurrencyAmountDTO;
import ru.verpul.DTO.CurrencyDTO;
import ru.verpul.enums.CurrencyType;
import ru.verpul.service.CurrencyService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDTO> getCurrencyRecords() {
        return currencyService.getCurrencyRecords();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCurrencyRecord(@RequestBody @Valid CurrencyDTO currencyDTO) {
        currencyService.saveCurrencyRecord(currencyDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrencyRecord(@RequestBody @Valid CurrencyDTO currencyDTO,
                                     @PathVariable Long id) {
        currencyService.updateCurrencyRecord(currencyDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrencyRecord(@PathVariable Long id) {
        currencyService.deleteCurrencyRecord(id);
    }

    @GetMapping("/calculate")
    public Map<CurrencyType, CurrencyAmountDTO> calculateCurrency() {return currencyService.calculateCurrency();
    }
}
