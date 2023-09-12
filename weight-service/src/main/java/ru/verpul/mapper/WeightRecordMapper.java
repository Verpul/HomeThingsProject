package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.model.WeightRecord;

@Mapper(componentModel = "spring")
public interface WeightRecordMapper {
    WeightRecord weightRecordDTOToWeightRecord(WeightRecordDTO weightRecordDTO);

    WeightRecordDTO weightRecordToWeightRecordDTO(WeightRecord weightRecord);
}
