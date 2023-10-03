package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.profile.Association;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.exception.AssociationFoundException;
import ru.verpul.exception.NotFoundException;
import ru.verpul.service.ReminderCategoryService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
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
        if (reminderCategoryService.updateReminderCategory(id, reminderCategoryDTO) == null) {
            throw new NotFoundException("Запись с id = " + id + " не найдена");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminderCategory(@PathVariable Long id) {
        int countAssociatedReminders = reminderCategoryService.deleteReminderCategory(id);
        if (countAssociatedReminders > 0) {
            throw new AssociationFoundException("Ссылка на эту запись у других объектов, количество:  " + countAssociatedReminders);
        }
    }
}
