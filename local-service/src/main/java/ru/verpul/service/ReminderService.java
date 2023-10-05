package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.ReminderMapper;
import ru.verpul.model.Reminder;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;
import ru.verpul.repository.ReminderRepository;

import java.util.*;
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
            reminders = reminderRepository.findAllOrderByParentIdAndExpireDate();
        } else {
            reminders = reminderRepository.findByCategory(categoryId);
        }

        List<ReminderDTO> reminderDTOList = reminders.stream()
                .map(reminderMapper::reminderToReminderDTO)
                .collect(Collectors.toList());

        return sortHierarchically(reminderDTOList);
    }

    @Transactional
    public void createReminder(ReminderDTO reminderDTO) {
        Reminder reminderToSave = reminderMapper.reminderDTOToReminder(reminderDTO);

        setReminderCategoryAndNestedDepth(reminderToSave, reminderDTO.getCategoryId());

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
            reminder.setParentId(reminderDTO.getParentId());

            setReminderCategoryAndNestedDepth(reminder, reminderDTO.getCategoryId());

            return reminderRepository.save(reminder);
        }

        return null;
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }

    private void setReminderCategoryAndNestedDepth(Reminder reminderToSave, Long reminderCategoryId) {
        if (reminderToSave.getParentId() != null) {
            Reminder parentReminder = reminderRepository.findById(reminderToSave.getParentId())
                    .orElseThrow(() -> new NotFoundException("Напоминание-родитель с такими данными не найдено!"));

            int nestingDepth = parentReminder.getNestingDepth() == null ? 1 : parentReminder.getNestingDepth() + 1;
            reminderToSave.setNestingDepth(nestingDepth);
            reminderToSave.setCategory(parentReminder.getCategory());
        } else {
            ReminderCategory categoryToSave = reminderCategoryId == null ?
                    null : reminderCategoryRepository.findById(reminderCategoryId).orElse(null);

            reminderToSave.setCategory(categoryToSave);
            reminderToSave.setNestingDepth(null);
        }
    }

    private List<ReminderDTO> sortHierarchically(List<ReminderDTO> reminderDTOList) {
        Map<Long, List<ReminderDTO>> remindersMap = new HashMap<>();

        for (ReminderDTO reminder : reminderDTOList) {
            if (!remindersMap.containsKey(reminder.getParentId())) {
                remindersMap.put(reminder.getParentId(), new ArrayList<>());
            }
            remindersMap.get(reminder.getParentId()).add(reminder);
        }

        List<ReminderDTO> sortedReminderDTOs = new ArrayList<>();
        sortTree(null, remindersMap, sortedReminderDTOs);

        return sortedReminderDTOs;
    }

    private void sortTree(Long parentId, Map<Long, List<ReminderDTO>> remindersMap, List<ReminderDTO> sortedReminders) {
        if (remindersMap.containsKey(parentId)) {
            for (ReminderDTO reminder : remindersMap.get(parentId)) {
                sortedReminders.add(reminder);
                sortTree(reminder.getId(), remindersMap, sortedReminders);
            }
        }
    }
}
