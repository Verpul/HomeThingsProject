package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.mapper.ReminderCategoryMapper;
import ru.verpul.model.Reminder;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;
import ru.verpul.repository.ReminderRepository;

import java.util.List;
import java.util.Optional;
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
    public ReminderCategory updateReminderCategory(Long id, ReminderCategoryDTO reminderCategoryDTO) {
        Optional<ReminderCategory> foundCategory =  reminderCategoryRepository.findById(id);

        if (foundCategory.isPresent()) {
            ReminderCategory reminderCategory = foundCategory.get();
            reminderCategory.setTitle(reminderCategoryDTO.getTitle());

            return reminderCategoryRepository.save(reminderCategory);
        }

        return null;
    }

    @Transactional
    public int deleteReminderCategory(Long id) {
        List<Reminder> foundReminders = reminderRepository.findAllByCategory(id);
        if (foundReminders.size() > 0) return foundReminders.size();

        reminderCategoryRepository.deleteById(id);
        return 0;
    }
}
