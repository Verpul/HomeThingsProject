package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.enums.ReminderPeriod;
import ru.verpul.exception.NotFoundException;
import ru.verpul.mapper.ReminderMapper;
import ru.verpul.model.Reminder;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;
import ru.verpul.repository.ReminderRepository;
import ru.verpul.util.ReminderUtil;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final ReminderCategoryRepository reminderCategoryRepository;

    public List<ReminderDTO> getRemindersList(Long categoryId, boolean showCompleted) {
        List<Reminder> reminders;

        if (categoryId == null) {
            reminders = showCompleted ?
                    reminderRepository.findAllOrderByParentIdAndExpireDate() :
                    reminderRepository.findUncompletedOrderByParentIdAndExpireDate();
        } else {
            reminders = showCompleted ?
                    reminderRepository.findAllByCategory(categoryId) :
                    reminderRepository.findUncompletedByCategory(categoryId);
        }

        List<ReminderDTO> reminderDTOList = reminders.stream()
                .map(reminderMapper::reminderToReminderDTO)
                .collect(Collectors.toList());

        return treeSort(reminderDTOList);
    }

    @Transactional
    public void createReminder(ReminderDTO reminderDTO) {
        Reminder reminderToSave = reminderMapper.reminderDTOToReminder(reminderDTO);

        setReminderCategoryAndNestedDepthAndParent(reminderToSave, reminderDTO);

        reminderRepository.save(reminderToSave);
    }

    @Transactional
    public void updateReminder(Long id, ReminderDTO reminderDTO) {
        reminderRepository.findById(id)
                .map(reminder -> {
                    reminder.setTitle(reminderDTO.getTitle());
                    reminder.setExpireDate(reminderDTO.getExpireDate());
                    reminder.setRemindDate(reminderDTO.getRemindDate());
                    reminder.setRemindTime(reminderDTO.getRemindTime());
                    reminder.setComment(reminderDTO.getComment());
                    reminder.setPeriodic(reminderDTO.getPeriodic());
                    reminder.setPeriodicity(reminderDTO.getPeriodicity());
                    reminder.setCompleted(reminderDTO.getCompleted());
                    setReminderCategoryAndNestedDepthAndParent(reminder, reminderDTO);

                    if (reminderDTO.getPeriodic()) {
                        reminder.setPeriod(ReminderPeriod.findByTitle(reminderDTO.getPeriod()));
                    }

                    return reminderRepository.save(reminder);
                }).orElseThrow(() -> new NotFoundException("Напоминание с id = " + id + " не найдено"));
    }

    @Transactional
    public void deleteReminder(Long id) {
        reminderRepository.deleteAll(reminderRepository.findReminderWithAllSiblings(id));
    }

    @Transactional
    public void changeCompleteStatus(Long id) {
        Reminder selectedReminder = reminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Напоминание с id = " + id + " не найдено"));
        boolean completed = selectedReminder.getCompleted();

        List<Reminder> remindersToChange = completed ?
                reminderRepository.findReminderWithParents(id) :
                reminderRepository.findReminderWithAllSiblings(id);

        List<Reminder> remindersToSave = remindersToChange.stream()
                .peek(reminder -> reminder.setCompleted(!completed)).collect(Collectors.toList());

        reminderRepository.saveAll(remindersToSave);

        if (selectedReminder.getPeriodic() && !completed) {
            Reminder newReminder = new Reminder();

            newReminder.setTitle(selectedReminder.getTitle());
            newReminder.setExpireDate(selectedReminder.getExpireDate());
            newReminder.setRemindDate(selectedReminder.getRemindDate());
            newReminder.setRemindTime(selectedReminder.getRemindTime());
            newReminder.setPeriodicity(selectedReminder.getPeriodicity());
            newReminder.setPeriodic(selectedReminder.getPeriodic());
            newReminder.setPeriod(selectedReminder.getPeriod());
            newReminder.setComment(selectedReminder.getComment());
            newReminder.setParent(selectedReminder.getParent());
            newReminder.setNestingDepth(selectedReminder.getNestingDepth());
            newReminder.setCategory(selectedReminder.getCategory());
            newReminder.setCompleted(false);

            ReminderUtil.setNextExpireAndRemindDate(newReminder);

            reminderRepository.save(newReminder);
        }
    }

    private void setReminderCategoryAndNestedDepthAndParent(Reminder reminderToSave, ReminderDTO reminderSource) {
        if (reminderSource.getParentId() != null) {
            Reminder parentReminder = reminderRepository.findById(reminderSource.getParentId())
                    .orElseThrow(() -> new NotFoundException("Напоминание-родитель с такими данными не найдено!"));

            int nestingDepth = parentReminder.getNestingDepth() == null ? 1 : parentReminder.getNestingDepth() + 1;
            reminderToSave.setNestingDepth(nestingDepth);
            reminderToSave.setCategory(parentReminder.getCategory());
            reminderToSave.setParent(parentReminder);
        } else {
            ReminderCategory categoryToSave = reminderSource.getCategoryId() == null ?
                    null : reminderCategoryRepository.findById(reminderSource.getCategoryId()).orElse(null);

            reminderToSave.setCategory(categoryToSave);
            reminderToSave.setNestingDepth(null);
        }
    }

    private List<ReminderDTO> treeSort(List<ReminderDTO> reminderDTOList) {
        Map<Long, List<ReminderDTO>> remindersMap = new HashMap<>();

        for (ReminderDTO reminder : reminderDTOList) {
            if (!remindersMap.containsKey(reminder.getParentId())) {
                remindersMap.put(reminder.getParentId(), new ArrayList<>());
            }
            remindersMap.get(reminder.getParentId()).add(reminder);
        }

        List<ReminderDTO> sortedReminderDTOs = new ArrayList<>();
        sortChildren(null, remindersMap, sortedReminderDTOs);

        return sortedReminderDTOs;
    }

    private void sortChildren(Long parentId, Map<Long, List<ReminderDTO>> remindersMap, List<ReminderDTO> sortedReminders) {
        if (remindersMap.containsKey(parentId)) {
            for (ReminderDTO reminder : remindersMap.get(parentId)) {
                sortedReminders.add(reminder);
                sortChildren(reminder.getId(), remindersMap, sortedReminders);
            }
        }
    }
}
