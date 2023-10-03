package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.mapper.ReminderMapper;
import ru.verpul.model.Reminder;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;
import ru.verpul.repository.ReminderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final ReminderCategoryRepository reminderCategoryRepository;

    public List<ReminderDTO> getRemindersList(Long categoryId) {
        List<Reminder> reminders;

        if (categoryId == null) {
            reminders = reminderRepository.findAll();
        } else {
            reminders = reminderRepository.findByCategory(categoryId);
        }

        return reminders.stream()
                .map(reminderMapper::reminderToReminderDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createReminder(ReminderDTO reminderDTO) {
        Reminder reminderToSave = reminderMapper.reminderDTOToReminder(reminderDTO);
        getReminderCategory(reminderDTO).ifPresent(reminderToSave::setCategory);

        reminderRepository.save(reminderToSave);
    }

    @Transactional
    public Reminder updateReminder(Long id, ReminderDTO reminderDTO) {
        Optional<Reminder> foundReminder = reminderRepository.findById(id);

        if (foundReminder.isPresent()) {
            Reminder reminder = foundReminder.get();
            reminder.setTitle(reminderDTO.getTitle());
            reminder.setExpireDate(reminderDTO.getExpireDate());
            reminder.setRemindDate(reminderDTO.getRemindDate());
            reminder.setRemindTime(reminderDTO.getRemindTime());
            reminder.setComment(reminderDTO.getComment());

            Optional<ReminderCategory> foundCategory = getReminderCategory(reminderDTO);
            reminder.setCategory(foundCategory.orElse(null));

            return reminderRepository.save(reminder);
        }

        return null;
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }

    private Optional<ReminderCategory> getReminderCategory(ReminderDTO reminderDTO) {
        if (reminderDTO.getCategoryId() == null) return Optional.empty();
        return reminderCategoryRepository.findById(reminderDTO.getCategoryId());
    }
}
