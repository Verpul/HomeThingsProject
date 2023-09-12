package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.WeightRecordMapper;
import ru.verpul.model.WeightRecord;
import ru.verpul.repository.WeightRecordRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final WeightRecordMapper weightRecordMapper;

    public List<WeightRecordDTO> getWeightRecordsList() {
        return weightRecordRepository.findAll()
                .stream()
                .map(weightRecordMapper::weightRecordToWeightRecordDTO)
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
            weightRecord.setWeightRecordValue(weightRecord.getWeightRecordValue());

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
}
