package ru.verpul.weight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.mapper.WeightRecordMapper;
import ru.verpul.model.WeightRecord;
import ru.verpul.repository.WeightRecordRepository;
import ru.verpul.service.WeightRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeightRecordServiceTest {

    @InjectMocks
    WeightRecordService weightRecordService;

    @Mock
    WeightRecordRepository weightRecordRepository;

    @Mock
    WeightRecordMapper weightRecordMapper;

    @Test
    void getWeightRecordsList() {
        when(weightRecordRepository.findAll(Sort.by("weightRecordDate").descending())).thenReturn(WeightRecordTestData.weightRecordList);

        List<WeightRecordDTO> weightRecordDTOList = weightRecordService.getWeightRecordsList();

        assertEquals(3, weightRecordDTOList.size());

        verify(weightRecordMapper, times(3)).weightRecordToWeightRecordDTO(any(WeightRecord.class), anyString());
    }

    @Test
    void createWeightRecord() {
        when(weightRecordRepository.save(any(WeightRecord.class))).thenReturn(WeightRecordTestData.weightRecordOne);
        when(weightRecordMapper.weightRecordDTOToWeightRecord(any(WeightRecordDTO.class))).thenReturn(WeightRecordTestData.weightRecordOne);

        weightRecordService.createWeightRecord(WeightRecordTestData.getNewWeightRecord());

        verify(weightRecordRepository).save(any(WeightRecord.class));
        verify(weightRecordMapper).weightRecordDTOToWeightRecord(any(WeightRecordDTO.class));
    }

    @Test
    void updateWeightRecord() {
        when(weightRecordRepository.findById(anyLong())).thenReturn(Optional.of(WeightRecordTestData.weightRecordOne));
        when(weightRecordRepository.save(any(WeightRecord.class))).thenReturn(WeightRecordTestData.weightRecordOne);

        WeightRecord updatedRecord = weightRecordService.updateWeightRecord(WeightRecordTestData.RECORD_ID, WeightRecordTestData.weightRecordDTOOne);

        assertNotNull(updatedRecord);
        assertEquals(WeightRecordTestData.weightRecordOne.getWeightRecordValue(), updatedRecord.getWeightRecordValue());

        verify(weightRecordRepository).findById(anyLong());
        verify(weightRecordRepository).save(any(WeightRecord.class));
    }

    @Test
    void updateWeightRecordNotFound() {
        when(weightRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        WeightRecord empty = weightRecordService.updateWeightRecord(WeightRecordTestData.RECORD_ID, WeightRecordTestData.weightRecordDTOOne);

        assertNull(empty);

        verify(weightRecordRepository).findById(anyLong());
        verifyNoMoreInteractions(weightRecordRepository);
    }

    @Test
    void deleteWeightRecord() {
        weightRecordService.deleteWeightRecord(WeightRecordTestData.RECORD_ID);

        verify(weightRecordRepository).deleteById(anyLong());
    }

    @Test
    void getWeightRecordByDate() {
        when(weightRecordRepository.findByRecordDate(any(LocalDate.class))).thenReturn(Optional.of(WeightRecordTestData.RECORD_ID));

        Long foundId = weightRecordService.getWeightRecordByDate(WeightRecordTestData.getNewWeightRecord().getWeightRecordDate());

        assertNotNull(foundId);
        assertEquals(foundId, WeightRecordTestData.RECORD_ID);

        verify(weightRecordRepository).findByRecordDate(any(LocalDate.class));
    }

    @Test
    void getWeightRecordByDateNotFound() {
        when(weightRecordRepository.findByRecordDate(any(LocalDate.class))).thenReturn(Optional.empty());

        Long foundId = weightRecordService.getWeightRecordByDate(WeightRecordTestData.getNewWeightRecord().getWeightRecordDate());

        assertNull(foundId);

        verify(weightRecordRepository).findByRecordDate(any(LocalDate.class));
    }

}
