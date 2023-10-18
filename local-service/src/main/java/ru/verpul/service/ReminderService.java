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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        return ReminderUtil.treeSort(reminderDTOList);
    }

    @Transactional
    public void createReminder(ReminderDTO reminderDTO) {
        setRemindDate(reminderDTO);

        Reminder reminderToSave = reminderMapper.reminderDTOToReminder(reminderDTO);
        setReminderCategoryAndNestedDepthAndParent(reminderToSave, reminderDTO);


        reminderRepository.save(reminderToSave);
    }

    @Transactional
    public void updateReminder(Long id, ReminderDTO reminderDTO) {
        reminderRepository.findById(id)
                .map(reminder -> {
                    reminder.setComment(reminderDTO.getComment());

                    if (!reminder.getCompleted()) {
                        reminder.setTitle(reminderDTO.getTitle());
                        reminder.setExpireDate(reminderDTO.getExpireDate());
                        setRemindDate(reminderDTO);
                        reminder.setRemindDate(reminderDTO.getRemindDate());
                        reminder.setRemindTime(reminderDTO.getRemindTime());
                        reminder.setPeriodic(reminderDTO.getPeriodic());
                        reminder.setPeriodicity(reminderDTO.getPeriodicity());
                        setReminderCategoryAndNestedDepthAndParent(reminder, reminderDTO);

                        if (reminderDTO.getPeriodic()) {
                            reminder.setPeriod(ReminderPeriod.findByTitle(reminderDTO.getPeriod()));
                        }
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
        if (reminderSource.getParentId() != null && reminderRepository.findById(reminderSource.getParentId()).isPresent()) {
            Reminder parentReminder = reminderRepository.findById(reminderSource.getParentId()).get();

            int nestingDepth = parentReminder.getNestingDepth() == null ? 1 : parentReminder.getNestingDepth() + 1;
            reminderToSave.setNestingDepth(nestingDepth);
            reminderToSave.setCategory(parentReminder.getCategory());
            reminderToSave.setParent(parentReminder);
        } else {
            ReminderCategory categoryToSave = reminderSource.getCategoryId() == null ?
                    null : reminderCategoryRepository.findById(reminderSource.getCategoryId()).orElse(null);

            if (reminderToSave.getId() != null && !Objects.equals(categoryToSave, reminderToSave.getCategory())) {
                List<Reminder> remindersToChange = reminderRepository.findReminderWithAllSiblings(reminderSource.getId());

                if (remindersToChange.size() != 0) {
                    List<Reminder> remindersToSave = remindersToChange.stream()
                            .filter(reminder -> !reminder.getId().equals(reminderSource.getId()))
                            .peek(reminder -> reminder.setCategory(categoryToSave)).collect(Collectors.toList());

                    reminderRepository.saveAll(remindersToSave);
                }
            }

            reminderToSave.setCategory(categoryToSave);
            reminderToSave.setNestingDepth(null);
        }
    }

    private void setRemindDate(ReminderDTO reminderDTO) {
        reminderDTO.setRemindDate(reminderDTO.getRemindTime() != null && reminderDTO.getRemindDate() == null ?
                LocalDate.now() : reminderDTO.getRemindDate());
    }

    public List<ReminderDTO> getRemindersWithProperRemindDate() {
        return reminderRepository.getReminderWithRemindDateAndTimeLessThanNow().stream()
                .map(reminderMapper::reminderToReminderDTO)
                .collect(Collectors.toList());

    }

    public List<ReminderDTO> getTimedRemindersForToday() {
        return reminderRepository.getRemindersForTodayWithRemindTime().stream()
                .map(reminderMapper::reminderToReminderDTO)
                .collect(Collectors.toList());
    }
}
