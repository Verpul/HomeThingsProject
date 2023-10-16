package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.service.ReminderService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/reminder", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping
    public List<ReminderDTO> getRemindersList(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                              @RequestParam(name = "showCompleted", defaultValue = "false") boolean showCompleted) {
        return reminderService.getRemindersList(categoryId, showCompleted);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createReminder(@Valid @RequestBody ReminderDTO reminderDTO) {
        reminderService.createReminder(reminderDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReminder(@PathVariable Long id, @Valid @RequestBody ReminderDTO reminderDTO) {
        reminderService.updateReminder(id, reminderDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCompleteStatus(@PathVariable Long id) {
        reminderService.changeCompleteStatus(id);
    }
}
