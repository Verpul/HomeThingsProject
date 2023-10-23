package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.service.ReminderCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/reminder/category", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReminderCategoryController {
    private final ReminderCategoryService reminderCategoryService;

    @GetMapping
    public List<ReminderCategoryDTO> getReminderCategories() {
        return reminderCategoryService.getReminderCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createReminderCategory(@Valid @RequestBody ReminderCategoryDTO reminderCategoryDTO) {
        reminderCategoryService.createReminderCategory(reminderCategoryDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReminderCategory(@PathVariable Long id,
                                       @Valid @RequestBody ReminderCategoryDTO reminderCategoryDTO) {
        reminderCategoryService.updateReminderCategory(id, reminderCategoryDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminderCategory(@PathVariable Long id) {
        reminderCategoryService.deleteReminderCategory(id);
    }
}
