package ru.verpul.service;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.exception.FileValidationException;
import ru.verpul.mapper.WeightRecordMapper;
import ru.verpul.model.WeightRecord;
import ru.verpul.repository.WeightRecordRepository;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

    public static final String VALID_DATE_REGEX = "^(0[1-9]|1\\d|2\\d|3[01])\\.(0[1-9]|1[0-2])\\.\\d{2}$";
    public static final String VALID_WEIGHT_VALUE_REGEX = "^(([5-9]\\d(\\.\\d)?)|(^\\s*))$";

    private final WeightRecordRepository weightRecordRepository;
    private final WeightRecordMapper weightRecordMapper;

    public List<WeightRecordDTO> getWeightRecordsList() {
        Sort sort = Sort.by("weightRecordDate").descending();
        List<WeightRecord> weightRecordList = weightRecordRepository.findAll(sort);

        int weightRecordListSize = weightRecordList.size();
        return IntStream.range(0, weightRecordListSize)
                .mapToObj(i -> {
                    String previousRecordValue = i == weightRecordListSize - 1 ? "0" : weightRecordList.get(i + 1).getWeightRecordValue();
                    return weightRecordMapper.weightRecordToWeightRecordDTO(weightRecordList.get(i), previousRecordValue);
                })
                .collect(Collectors.toList());
    }

    public void createWeightRecord(WeightRecordDTO weightRecordDTO) {
        weightRecordRepository.save(weightRecordMapper.weightRecordDTOToWeightRecord(weightRecordDTO));
    }

    @Transactional
    public WeightRecord updateWeightRecord(Long id, WeightRecordDTO weightRecordDTO) {
        Optional<WeightRecord> foundWeightRecord = weightRecordRepository.findById(id);

        if (foundWeightRecord.isPresent()) {
            WeightRecord weightRecord = foundWeightRecord.get();
            weightRecord.setWeightRecordDate(weightRecordDTO.getWeightRecordDate());
            weightRecord.setWeightRecordValue(weightRecordDTO.getWeightRecordValue());

            return weightRecordRepository.save(weightRecord);
        }

        return null;
    }

    public void deleteWeightRecord(Long id) {
        weightRecordRepository.deleteById(id);
    }

    public Long getWeightRecordByDate(LocalDate recordDate) {
        return weightRecordRepository.findByRecordDate(recordDate).orElse(null);
    }

    @Transactional
    public void handleFileData(MultipartFile file) {
        List<String> dataErrors = new ArrayList<>();
        int lineNumber = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            List<WeightRecordDTO> weightRecords = new ArrayList<>();
            String[] line;

            while ((line = reader.readNext()) != null) {
                lineNumber++;
                String recordDateAsString = line[0];
                String recordValue = line[1];

                if (!recordDateAsString.matches(VALID_DATE_REGEX)
                        || !recordValue.matches(VALID_WEIGHT_VALUE_REGEX)
                        || LocalDate.parse(recordDateAsString, dateTimeFormatter).isAfter(LocalDate.now())) {
                    String errorMessage = "Некорректные данные в строке " + lineNumber + ": " + recordDateAsString + "," + recordValue;
                    dataErrors.add(errorMessage);
                    continue;
                }

                LocalDate recordDate = LocalDate.parse(recordDateAsString, dateTimeFormatter);
                WeightRecordDTO weightRecord = WeightRecordDTO.builder()
                        .weightRecordDate(recordDate)
                        .weightRecordValue(recordValue)
                        .build();

                weightRecords.add(weightRecord);
            }

            List<WeightRecord> recordsToSave = weightRecords.stream()
                    .map(weightRecordMapper::weightRecordDTOToWeightRecord)
                    .collect(Collectors.toList());

            saveFileData(recordsToSave);

            if (!dataErrors.isEmpty()) throw new Exception();
        } catch (Exception e) {
            String message = "Неверный формат файла";

            if (!dataErrors.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Обработано ").append(lineNumber).append(" строк").append("\n");
                sb.append("Из них ").append(dataErrors.size()).append(" не были загружены:").append("\n");

                for (String error: dataErrors) {
                    sb.append(error).append("\n");
                }
                message = sb.toString();
            }

            throw new FileValidationException(message);
        }
    }

    private void saveFileData(List<WeightRecord> recordsToSave) {
        List<WeightRecord> savedRecords = weightRecordRepository.findAll();
        recordsToSave = recordsToSave.stream()
                .peek(recordToSave -> {
                    Optional<WeightRecord> existingRecord = savedRecords.stream()
                            .filter(savedRecord -> savedRecord.getWeightRecordDate().equals(recordToSave.getWeightRecordDate()))
                            .findFirst();

                    existingRecord.ifPresent(weightRecord -> recordToSave.setId(weightRecord.getId()));

                })
                .collect(Collectors.toList());

        weightRecordRepository.saveAll(recordsToSave);
    }
}
























