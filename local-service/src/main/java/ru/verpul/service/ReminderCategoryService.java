package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.exception.AssociationFoundException;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.ReminderCategoryMapper;
import ru.verpul.model.Reminder;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;
import ru.verpul.repository.ReminderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderCategoryService {
    private final ReminderCategoryRepository reminderCategoryRepository;
    private final ReminderCategoryMapper reminderCategoryMapper;
    private final ReminderRepository reminderRepository;

    public List<ReminderCategoryDTO> getReminderCategories() {
        return reminderCategoryRepository.findAll().stream()
                .map(reminderCategoryMapper::reminderCategoryToReminderCategoryDTO)
                .collect(Collectors.toList());
    }

    public void createReminderCategory(ReminderCategoryDTO reminderCategoryDTO) {
        reminderCategoryRepository.save(reminderCategoryMapper.reminderCategoryDTOToReminderCategory(reminderCategoryDTO));
    }

    @Transactional
    public void updateReminderCategory(Long id, ReminderCategoryDTO reminderCategoryDTO) {
        reminderCategoryRepository.findById(id)
                .map(reminderCategory -> {
                    reminderCategory.setTitle(reminderCategoryDTO.getTitle());

                    return reminderCategoryRepository.save(reminderCategory);
                }).orElseThrow(() -> new NotFoundException("Категория с id = " + id + " не найдена"));
    }

    @Transactional
    public void deleteReminderCategory(Long id) {
        List<Reminder> foundReminders = reminderRepository.findAllByCategory(id);

        if (foundReminders.size() > 0) {
            throw new AssociationFoundException("У этой категории " + foundReminders.size() + " напоминаний, удаление невозможно");
        } else {
            reminderCategoryRepository.deleteById(id);
        }
    }

    public ReminderCategory findCategoryByTitle(String title) {
        return reminderCategoryRepository.findCategoryByTitle(title);
    }
}
