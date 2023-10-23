package ru.verpul.weight;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.controller.WeightRecordController;
import ru.verpul.service.WeightRecordService;


import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeightRecordController.class)
@AutoConfigureMockMvc
public class WeightRecordControllerTest {
    private static final String WEIGHT_RECORDS_REST_URL = "/api/weight/";
    private static final String VALIDATION_NULL_MESSAGE = "Поле должно быть заполнено";
    private static final String VALIDATION_EXISTS_MESSAGE = "Дата записи не уникальна";
    private static final String VALIDATION_WRONG_FORMAT_MESSAGE = "Вес должен быть от 50 до 99 кг ровно или с одой цифрой после точки";
    private static final String VALIDATION_DATE_FROM_FUTURE = "Дата взвешивания не может быть больше текущей";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WeightRecordService weightRecordService;

    @Test
    void getWeightRecordsList() throws Exception {
        when(weightRecordService.getWeightRecordsList()).thenReturn(WeightRecordTestData.weightRecordDTOList);

        mockMvc.perform(get(WEIGHT_RECORDS_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void createWeightRecord() throws Exception {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();

        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(null);
        doNothing().when(weightRecordService).createWeightRecord(any(WeightRecordDTO.class));

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createWeightRecordNullRecordDate() throws Exception {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();
        newWeightRecordDTO.setWeightRecordDate(null);

        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(null);

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightRecordDate", is(VALIDATION_NULL_MESSAGE)));
    }

    @Test
    void createWeightRecordNullRecordValue() throws Exception {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();
        newWeightRecordDTO.setWeightRecordValue(null);

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightRecordValue", is(VALIDATION_NULL_MESSAGE)));
    }

    @Test
    void createWeightRecordAlreadyExistsRecordDate()throws Exception  {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();

        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(WeightRecordTestData.RECORD_ID);

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightRecordDate", is(VALIDATION_EXISTS_MESSAGE)));
    }

    @ParameterizedTest
    @CsvSource({"123", "string", "0", "70.99", "09.99", "99."})
    void createWeightRecordWrongFormatRecordValue(String value) throws Exception {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();
        newWeightRecordDTO.setWeightRecordValue(value);

        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(null);

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightRecordValue", is(VALIDATION_WRONG_FORMAT_MESSAGE)));
    }

    @Test
    void createWeightRecordDateFuture() throws Exception {
        WeightRecordDTO newWeightRecordDTO = WeightRecordTestData.getNewWeightRecord();
        newWeightRecordDTO.setWeightRecordDate(LocalDate.parse("3000-01-01"));

        mockMvc.perform(post(WEIGHT_RECORDS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeightRecordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightRecordDate", is(VALIDATION_DATE_FROM_FUTURE)));
    }

    @Test
    void updateWeightRecord() throws Exception {
        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(null);
        when(weightRecordService.updateWeightRecord(anyLong(), any(WeightRecordDTO.class))).thenReturn(WeightRecordTestData.weightRecordOne);

        mockMvc.perform(put(WEIGHT_RECORDS_REST_URL + WeightRecordTestData.RECORD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WeightRecordTestData.weightRecordDTOOne)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateWeightRecordNotFound() throws Exception {
        when(weightRecordService.getWeightRecordByDate(any(LocalDate.class))).thenReturn(null);
        when(weightRecordService.updateWeightRecord(anyLong(), any(WeightRecordDTO.class))).thenReturn(null);

        mockMvc.perform(put(WEIGHT_RECORDS_REST_URL + WeightRecordTestData.RECORD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WeightRecordTestData.weightRecordDTOOne)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWeightRecord() throws Exception {
        doNothing().when(weightRecordService).deleteWeightRecord(anyLong());

        mockMvc.perform(delete(WEIGHT_RECORDS_REST_URL + WeightRecordTestData.RECORD_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
