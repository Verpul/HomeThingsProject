package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.CurrencyDTO;
import ru.verpul.model.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    Currency currencyDTOtoCurrency(CurrencyDTO currencyDTO);

    CurrencyDTO currencyToCurrencyDTO(Currency currency);
}
