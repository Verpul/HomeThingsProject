package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.mapper.ReminderMapper;
import ru.verpul.model.Reminder;
import ru.verpul.repository.ReminderRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    public Set<ReminderDTO> getRemindersList() {
        return reminderMapper.remindersListToRemindersDTOSet(reminderRepository.findAll());
    }

    public void createReminder(ReminderDTO reminderDTO) {
        reminderRepository.save(reminderMapper.reminderDTOToReminder(reminderDTO));
    }

    public Reminder updateReminder(Long id, ReminderDTO reminderDTO) {
        Optional<Reminder> foundReminder = reminderRepository.findById(id);

        if (foundReminder.isPresent()) {
            Reminder reminder = foundReminder.get();
            reminder.setTitle(reminderDTO.getTitle());
            reminder.setExpireDate(reminderDTO.getExpireDate());

            return reminderRepository.save(reminder);
        }

        return null;
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }
}
