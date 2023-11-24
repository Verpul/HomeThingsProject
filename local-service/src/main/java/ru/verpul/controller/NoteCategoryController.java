package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.NoteCategoryDTO;
import ru.verpul.service.NoteCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/note/category", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteCategoryController {
    private final NoteCategoryService noteCategoryService;

    @GetMapping
    public List<NoteCategoryDTO> getNotesCategories() {
        return noteCategoryService.getNotesCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNoteCategory(@Valid @RequestBody NoteCategoryDTO categoryToSave) {
        noteCategoryService.createNoteCategory(categoryToSave);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNoteCategory(@Valid @RequestBody NoteCategoryDTO categoryToUpdate,
                                   @PathVariable Long categoryId) {
        noteCategoryService.updateNoteCategory(categoryToUpdate, categoryId);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNoteCategory(@PathVariable Long categoryId) {
        noteCategoryService.deleteNoteCategory(categoryId);
    }
}
