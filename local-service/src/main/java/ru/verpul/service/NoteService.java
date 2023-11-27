package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.NoteDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.NoteMapper;
import ru.verpul.repository.NoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public List<NoteDTO> getNotesForCategory(Long categoryId) {
        return noteRepository.findByCategoryId(categoryId).stream()
                .map(noteMapper::noteToNoteDTO)
                .collect(Collectors.toList());
    }

    public void save(NoteDTO note) {
        noteRepository.save(noteMapper.noteDTOToNote(note));
    }

    public void updateNote(NoteDTO note, Long noteId) {
        noteRepository.findById(noteId)
                .map(foundCategory -> {
                    foundCategory.setText(note.getText());

                    return noteRepository.save(foundCategory);
                }).orElseThrow(() -> new NotFoundException("Категория заметок с id = " + noteId + " не найдена"));
    }

    public void deleteNote(Long noteId) {
        noteRepository.findById(noteId).ifPresent(noteRepository::delete);
    }
}
