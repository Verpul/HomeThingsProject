package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.WeightRecordMapper;
import ru.verpul.model.WeightRecord;
import ru.verpul.repository.WeightRecordRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

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
}
