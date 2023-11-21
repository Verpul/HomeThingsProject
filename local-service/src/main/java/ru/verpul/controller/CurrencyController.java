package ru.verpul.controller;

import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.CurrencyAmountDTO;
import ru.verpul.DTO.CurrencyDTO;
import ru.verpul.enums.CurrencyType;
import ru.verpul.service.CurrencyService;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/currency")
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

    @GetMapping("/xls")
    public ResponseEntity<ByteArrayResource> getXLSFile() {
        ByteArrayOutputStream stream = currencyService.getXLSFile();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDisposition(ContentDisposition.attachment().filename("CurrencyRecords.xlsx").build());

        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
    }
}
