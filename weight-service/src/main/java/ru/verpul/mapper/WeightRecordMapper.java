package ru.verpul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.format.annotation.DateTimeFormat;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.model.WeightRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface WeightRecordMapper {
    WeightRecord weightRecordDTOToWeightRecord(WeightRecordDTO weightRecordDTO);

    @Mapping(target = "differencePreviousValue",
            expression = "java(this.countDifferenceFromPreviousValue(weightRecord.getWeightRecordValue(), previousValue))")
    @Mapping(target = "formattedWeightRecordDate", source = "weightRecord.weightRecordDate", qualifiedByName = "formatWeightRecordDate")
    WeightRecordDTO weightRecordToWeightRecordDTO(WeightRecord weightRecord, String previousValue);

    @Named("formatWeightRecordDate")
    default String formatWeightRecordDate(LocalDate weightRecordDate) {
        return weightRecordDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    default Double countDifferenceFromPreviousValue(String currentValue, String previousValue) {
        BigDecimal bdCurrentValue = new BigDecimal(currentValue);
        BigDecimal bdPreviousValue = new BigDecimal(previousValue);

        return bdCurrentValue.subtract(bdPreviousValue).doubleValue();
    }
}
