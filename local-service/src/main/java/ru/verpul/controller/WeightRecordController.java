package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.exception.FileValidationException;
import ru.verpul.exception.NotFoundException;
import ru.verpul.service.WeightRecordService;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/weight", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WeightRecordController {
    private static final long MAX_FILE_SIZE = 1048576;
    private static final String ALLOWED_FILE_TYPE = "text/csv";

    private final WeightRecordService weightRecordService;

    @GetMapping
    public List<WeightRecordDTO> getWeightRecordsList() {
        return weightRecordService.getWeightRecordsList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWeightRecord(@Valid @RequestBody WeightRecordDTO weightRecordDTO) {
        weightRecordService.createWeightRecord(weightRecordDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWeightRecord(@PathVariable Long id, @Valid @RequestBody WeightRecordDTO weightRecordDTO) {
        if (weightRecordService.updateWeightRecord(id, weightRecordDTO) == null) {
            throw new NotFoundException("Запись с id = " + id + " не найдена");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWeightRecord(@PathVariable Long id) {
        weightRecordService.deleteWeightRecord(id);
    }

    @PostMapping("/upload")
    public void uploadDataFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) throw new FileValidationException("Файл не может быть пустым");
        if (!Objects.equals(file.getContentType(), ALLOWED_FILE_TYPE)) throw new FileValidationException("Файл должен быть формата CSV");
        if (file.getSize() > MAX_FILE_SIZE) throw new FileValidationException("Размер файла не может превышать 1 Мб");

        weightRecordService.handleFileData(file);
    }
}
