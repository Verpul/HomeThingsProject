package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.NoteCategoryDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.NoteCategoryMapper;
import ru.verpul.repository.NoteCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteCategoryService {
    private final NoteCategoryRepository noteCategoryRepository;
    private final NoteCategoryMapper noteCategoryMapper;

    public List<NoteCategoryDTO> getNotesCategories() {
        return noteCategoryRepository.findAll().stream()
                .map(noteCategoryMapper::noteCategoryToNoteCategoryDTO)
                .collect(Collectors.toList());
    }

    public void createNoteCategory(NoteCategoryDTO noteToSave) {
        noteCategoryRepository.save(noteCategoryMapper.noteCategoryDTOToNoteCategory(noteToSave));
    }

    @Transactional
    public void updateNoteCategory(NoteCategoryDTO noteToUpdate, Long id) {
        noteCategoryRepository.findById(id)
                .map(foundCategory -> {
                    foundCategory.setTitle(noteToUpdate.getTitle());

                    return noteCategoryRepository.save(foundCategory);
                }).orElseThrow(() -> new NotFoundException("Категория заметок с id = " + id + " не найдена"));
    }

    @Transactional
    public void deleteNoteCategory(Long id) {
        noteCategoryRepository.findById(id).ifPresent(noteCategoryRepository::delete);
    }

    public Long findByTitle(String title) {
        return noteCategoryRepository.findByTitle(title);
    }
}
