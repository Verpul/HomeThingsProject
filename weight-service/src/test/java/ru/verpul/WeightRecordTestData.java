package ru.verpul;

import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.model.WeightRecord;

import java.time.LocalDate;
import java.util.List;

public class WeightRecordTestData {
    public static final WeightRecord weightRecordOne = new WeightRecord(1L, LocalDate.parse("2023-01-01"), "65.5");
    public static final WeightRecord weightRecordTwo = new WeightRecord(2L, LocalDate.parse("2023-01-01"), "70.1");
    public static final WeightRecord weightRecordThree= new WeightRecord(3L, LocalDate.parse("2023-01-01"), "68");
    public static final List<WeightRecord> weightRecordList = List.of(weightRecordOne, weightRecordTwo, weightRecordThree);
    public static final WeightRecordDTO weightRecordDTOOne = new WeightRecordDTO(1L, LocalDate.parse("2023-01-01"), "65.5");
    public static final WeightRecordDTO weightRecordDTOTwo = new WeightRecordDTO(2L, LocalDate.parse("2023-01-01"), "70.1");
    public static final WeightRecordDTO weightRecordDTOThree = new WeightRecordDTO(3L, LocalDate.parse("2023-01-01"), "68");
    public static final List<WeightRecordDTO> weightRecordDTOList = List.of(weightRecordDTOOne, weightRecordDTOTwo, weightRecordDTOThree);

    public static WeightRecordDTO getNewWeightRecord() {
        return new WeightRecordDTO(null, LocalDate.parse("2023-01-01"), "65.5");
    }

    public static final Long RECORD_ID = 1L;
}
