package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.service.WeightRecordService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/weight", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WeightRecordController {
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
}
