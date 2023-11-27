package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.NoteDTO;
import ru.verpul.service.NoteService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/note", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/{categoryId}")
    public List<NoteDTO> getNotesForCategory(@PathVariable Long categoryId) {
        return noteService.getNotesForCategory(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNote(@RequestBody NoteDTO note) {
        noteService.save(note);
    }

    @PutMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNote(@RequestBody NoteDTO note,
                           @PathVariable Long noteId) {
        noteService.updateNote(note, noteId);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
    }
}
