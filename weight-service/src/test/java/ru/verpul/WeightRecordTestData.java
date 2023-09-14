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
    public static final WeightRecordDTO weightRecordDTOOne = new WeightRecordDTO(1L, LocalDate.parse("2023-01-03"), "69.9", -0.2, "03.01.2023");
    public static final WeightRecordDTO weightRecordDTOTwo = new WeightRecordDTO(2L, LocalDate.parse("2023-01-02"), "70.1", 2.1, "02.01.2023");
    public static final WeightRecordDTO weightRecordDTOThree = new WeightRecordDTO(3L, LocalDate.parse("2023-01-01"), "68", 68D, "01.01.2023");
    public static final List<WeightRecordDTO> weightRecordDTOList = List.of(weightRecordDTOOne, weightRecordDTOTwo, weightRecordDTOThree);

    public static WeightRecordDTO getNewWeightRecord() {
        return WeightRecordDTO.builder()
                .weightRecordDate(LocalDate.parse("2023-01-01"))
                .weightRecordValue("65.5")
                .build();
    }

    public static final Long RECORD_ID = 1L;
}
