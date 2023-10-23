package ru.verpul.reminder;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.R;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;
import ru.verpul.repository.ReminderRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.verpul.reminder.ReminderTestData.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/devdata/schema.sql", "classpath:data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@AutoConfigureMockMvc
public class ReminderTest {
    private static final String REMINDERS_REST_URL = "/api/reminder";
    private static final String REMINDERS_REST_URL_WITH_ID = REMINDERS_REST_URL + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private ReminderRepository reminderRepository;

    @Test
    void getRemindersListUncompletedWithoutCategory() throws Exception {
        mockMvc.perform(get(REMINDERS_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(12)))
                .andExpect(jsonPath("$[11].completed", is(false)))
                .andExpect(jsonPath("$[0].expireDate", is("01.01.2024")))
                .andExpect(jsonPath("$[1].title", containsString("Поднапоминание 2 уровня")))
                .andExpect(jsonPath("$[2].title", containsString("Поднапоминание")))
                .andExpect(jsonPath("$[3].title", containsString("Поднапоминание")))
                .andExpect(jsonPath("$[4].title", containsString("Поднапоминание")))
                .andExpect(jsonPath("$[5].title", containsString("Поднапоминание")))
                .andExpect(jsonPath("$[9].declensionPeriod", containsString("30 дней")));
    }

    @Test
    void getRemindersListWithCompletedWithoutCategory() throws Exception {
        mockMvc.perform(get(REMINDERS_REST_URL)
                        .param(PARAM_SHOW_COMPLETED, "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[14].completed", is(true)));
    }

    @Test
    void getRemindersListUncompletedWithCategory() throws Exception {
        mockMvc.perform(get(REMINDERS_REST_URL)
                        .param(PARAM_CATEGORY_ID, String.valueOf(CATEGORY_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getRemindersListCompletedWithCategory() throws Exception {
        mockMvc.perform(get(REMINDERS_REST_URL)
                        .param(PARAM_SHOW_COMPLETED, "true")
                        .param(PARAM_CATEGORY_ID, String.valueOf(CATEGORY_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].completed", is(true)));
    }

    @Test
    @Transactional
    @Rollback
    void createReminderAllFieldsSuccess() throws Exception {
        ReminderDTO reminderToSave = getNewReminderDTO();

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToSave)))
                .andExpect(status().isCreated());

        Optional<Reminder> savedReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(savedReminder.isPresent());
        assertEquals(savedReminder.get().getTitle(), reminderToSave.getTitle());
        assertNull(savedReminder.get().getParent());
        assertNull(savedReminder.get().getNestingDepth());
        assertEquals(savedReminder.get().getCategory().getId(), reminderToSave.getCategoryId());
        assertEquals(savedReminder.get().getRemindDate(), reminderToSave.getRemindDate());
        assertEquals(savedReminder.get().getRemindTime(), reminderToSave.getRemindTime());
        assertEquals(savedReminder.get().getExpireDate(), reminderToSave.getExpireDate());
        assertEquals(savedReminder.get().getComment(), reminderToSave.getComment());
        assertTrue(savedReminder.get().getPeriodic());
        assertEquals(savedReminder.get().getPeriod().getTitle(), reminderToSave.getPeriod());
        assertEquals(savedReminder.get().getPeriodicity(), reminderToSave.getPeriodicity());
        assertFalse(savedReminder.get().getCompleted());
    }

    @Test
    @Transactional
    @Rollback
    void createReminderWithReminderTimeWithoutRemindDateSuccess() throws Exception {
        ReminderDTO reminderToSave = getNewReminderDTO();
        reminderToSave.setRemindDate(null);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToSave)))
                .andExpect(status().isCreated());

        Optional<Reminder> savedReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(savedReminder.isPresent());
        assertEquals(savedReminder.get().getRemindDate(), LocalDate.now());
        assertEquals(savedReminder.get().getRemindTime(), reminderToSave.getRemindTime());
    }

    @Test
    @Transactional
    @Rollback
    void createReminderWithParentIdSuccess() throws Exception {
        ReminderDTO reminderToSave = getNewReminderDTO();
        reminderToSave.setParentId(PARENT_ID);
        reminderToSave.setCategoryId(null);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToSave)))
                .andExpect(status().isCreated());

        Optional<Reminder> savedReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(savedReminder.isPresent());
        assertEquals(savedReminder.get().getParent().getId(), reminderToSave.getParentId());
        assertEquals(savedReminder.get().getNestingDepth(), SAVED_REMINDER_NESTING_DEPTH);
        assertEquals(savedReminder.get().getCategory().getId(), CATEGORY_ID);
    }

    @Test
    @Transactional
    @Rollback
    void createReminderParentIdAndCategoryNotFoundSuccess() throws Exception {
        ReminderDTO reminderToSave = getNewReminderDTO();
        reminderToSave.setParentId(NOT_FOUND_ID);
        reminderToSave.setCategoryId(NOT_FOUND_ID);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToSave)))
                .andExpect(status().isCreated());

        Optional<Reminder> savedReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(savedReminder.isPresent());
        assertNull(savedReminder.get().getParent());
        assertNull(savedReminder.get().getCategory());
    }

    @Test
    @Transactional
    @Rollback
    void createReminderCompletedAndNestingDepthSetSuccess() throws Exception {
        ReminderDTO reminderToSave = getNewReminderDTO();
        reminderToSave.setNestingDepth(5);
        reminderToSave.setCompleted(true);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToSave)))
                .andExpect(status().isCreated());

        Optional<Reminder> savedReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(savedReminder.isPresent());
        assertNull(savedReminder.get().getNestingDepth());
        assertFalse(savedReminder.get().getCompleted());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void createReminderTitleValidationNotBlankFail(String title) throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setTitle(title);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Название должно быть заполнено")));
    }

    @Test
    void createReminderTitleValidationMaxSizeFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setTitle(reminder.getTitle().repeat(100));

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Название не может превышать 200 символов")));
    }

    @Test
    void createReminderReminderDateValidationPresentOrFutureFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setRemindDate(LocalDate.of(2000, 1, 1));

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.remindDate", is("Дата напоминания не может быть меньше текущей")));
    }

    @Test
    void createReminderReminderTimeValidationFutureFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setRemindDate(LocalDate.now());
        reminder.setRemindTime(LocalTime.now());

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.remindTime", is("Время напоминания не может быть меньше текущего")));
    }


    @Test
    void createReminderExpireDateValidationFeatureOrPresentFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setExpireDate(LocalDate.of(2000, 1, 1));

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.expireDate", is("Дата события не может быть меньше текущей")));
    }

    @Test
    void createReminderCommentValidationSizeFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setComment(reminder.getComment().repeat(500));

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.comment", is("Комментарий не может превышать 500 символов")));
    }

    @Test
    void createReminderPeriodicityValidationPositiveFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setPeriodicity(-1);

        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.periodicity", is("Периодичность не может быть меньше 1")));
    }

    @Test
    void PeriodicPeriodNullFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setPeriod(null);

        createReminderPeriodicValidationPeriodicReminderFail(reminder);
    }

    @Test
    void PeriodicPeriodicityNullFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setPeriodicity(null);

        createReminderPeriodicValidationPeriodicReminderFail(reminder);
    }

    @Test
    void PeriodicExpireDateNullFail() throws Exception {
        ReminderDTO reminder = getNewReminderDTO();
        reminder.setExpireDate(null);

        createReminderPeriodicValidationPeriodicReminderFail(reminder);
    }

    void createReminderPeriodicValidationPeriodicReminderFail(ReminderDTO reminder) throws Exception {
        mockMvc.perform(post(REMINDERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.periodic", is("Период, периодичность и дата события должны быть заполнены")));
    }

    @Test
    @Transactional
    @Rollback
    void updateUncompletedAllFieldsSuccess() throws Exception {
        ReminderDTO reminderToUpdate = getNewReminderDTO();
        reminderToUpdate.setId(REMINDER_TO_UPDATE_UNCOMPLETED_ID);

        mockMvc.perform(put(REMINDERS_REST_URL_WITH_ID, REMINDER_TO_UPDATE_UNCOMPLETED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToUpdate)))
                .andExpect(status().isNoContent());

        Optional<Reminder> updatedReminder = reminderRepository.findById(REMINDER_TO_UPDATE_UNCOMPLETED_ID);

        assertTrue(updatedReminder.isPresent());
        assertEquals(updatedReminder.get().getId(), REMINDER_TO_UPDATE_UNCOMPLETED_ID);
        assertEquals(updatedReminder.get().getTitle(), reminderToUpdate.getTitle());
        assertNull(updatedReminder.get().getParent());
        assertNull(updatedReminder.get().getNestingDepth());
        assertEquals(updatedReminder.get().getCategory().getId(), reminderToUpdate.getCategoryId());
        assertEquals(updatedReminder.get().getRemindDate(), reminderToUpdate.getRemindDate());
        assertEquals(updatedReminder.get().getRemindTime(), reminderToUpdate.getRemindTime());
        assertEquals(updatedReminder.get().getExpireDate(), reminderToUpdate.getExpireDate());
        assertEquals(updatedReminder.get().getComment(), reminderToUpdate.getComment());
        assertTrue(updatedReminder.get().getPeriodic());
        assertEquals(updatedReminder.get().getPeriod().getTitle(), reminderToUpdate.getPeriod());
        assertEquals(updatedReminder.get().getPeriodicity(), reminderToUpdate.getPeriodicity());
        assertFalse(updatedReminder.get().getCompleted());
    }

    @Test
    void updateCompletedOnlyCommentSuccess() throws Exception{
        ReminderDTO reminderToUpdate = getNewReminderDTO();
        reminderToUpdate.setId(REMINDER_TO_UPDATE_COMPLETED_ID);

        mockMvc.perform(put(REMINDERS_REST_URL_WITH_ID, REMINDER_TO_UPDATE_COMPLETED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToUpdate)))
                .andExpect(status().isNoContent());

        Optional<Reminder> updatedReminder = reminderRepository.findById(REMINDER_TO_UPDATE_COMPLETED_ID);

        assertTrue(updatedReminder.isPresent());
        assertNotEquals(updatedReminder.get().getTitle(), reminderToUpdate.getTitle());
        assertNotEquals(updatedReminder.get().getRemindDate(), reminderToUpdate.getRemindDate());
        assertNotEquals(updatedReminder.get().getRemindTime(), reminderToUpdate.getRemindTime());
        assertNotEquals(updatedReminder.get().getExpireDate(), reminderToUpdate.getExpireDate());
        assertFalse(updatedReminder.get().getPeriodic());
        assertNull(updatedReminder.get().getPeriod());
        assertNotEquals(updatedReminder.get().getPeriodicity(), reminderToUpdate.getPeriodicity());

        assertTrue(updatedReminder.get().getCompleted());
        assertEquals(updatedReminder.get().getComment(), reminderToUpdate.getComment());
    }

    @Test
    @Transactional
    @Rollback
    void updateCategoryOfChildrenWhenParentCategoryChangesSuccess() throws Exception{
        ReminderDTO reminderToUpdate = getNewReminderDTO();
        reminderToUpdate.setId(REMINDER_WITH_CHILDREN_ID);

        mockMvc.perform(put(REMINDERS_REST_URL_WITH_ID, REMINDER_WITH_CHILDREN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToUpdate)))
                .andExpect(status().isNoContent());

        Optional<Reminder> parentReminderUpdated = reminderRepository.findById(REMINDER_WITH_CHILDREN_ID);
        Optional<Reminder> childOneReminderUpdated = reminderRepository.findById(REMINDER1_CHILD_ID);
        Optional<Reminder> childTwoReminderUpdated = reminderRepository.findById(REMINDER2_CHILD_ID);

        assertEquals(parentReminderUpdated.get().getCategory().getId(), CATEGORY_ID);
        assertEquals(childOneReminderUpdated.get().getCategory().getId(), parentReminderUpdated.get().getCategory().getId());
        assertEquals(childTwoReminderUpdated.get().getCategory().getId(), parentReminderUpdated.get().getCategory().getId());
    }

    @Test
    void updateNotFoundFail() throws Exception {
        ReminderDTO reminderToUpdate = getNewReminderDTO();
        reminderToUpdate.setId(NOT_FOUND_ID);

        mockMvc.perform(put(REMINDERS_REST_URL_WITH_ID, NOT_FOUND_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reminderToUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Напоминание с id = " + NOT_FOUND_ID + " не найдено")));
    }

    @Test
    @Transactional
    @Rollback
    void deleteSingleReminderSuccess() throws Exception {
        mockMvc.perform(delete(REMINDERS_REST_URL_WITH_ID, PARENT_ID))
                .andExpect(status().isNoContent());

        Optional<Reminder> deletedReminder = reminderRepository.findById(PARENT_ID);

        assertFalse(deletedReminder.isPresent());
    }

    @Test
    void deleteReminderWithChildrenSuccess() throws Exception {
        mockMvc.perform(delete(REMINDERS_REST_URL_WITH_ID, REMINDER_WITH_CHILDREN_ID))
                .andExpect(status().isNoContent());

        Optional<Reminder> deletedParentReminder = reminderRepository.findById(REMINDER_WITH_CHILDREN_ID);
        Optional<Reminder> deletedChildOneReminder = reminderRepository.findById(REMINDER1_CHILD_ID);
        Optional<Reminder> deletedChildTwoReminder = reminderRepository.findById(REMINDER2_CHILD_ID);

        assertFalse(deletedParentReminder.isPresent());
        assertFalse(deletedChildOneReminder.isPresent());
        assertFalse(deletedChildTwoReminder.isPresent());
    }

    @Test
    void deleteReminderNotFoundSuccess() throws Exception {
        mockMvc.perform(delete(REMINDERS_REST_URL_WITH_ID, NOT_FOUND_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @Rollback
    void changeCompleteStatusSingleReminderSuccess() throws Exception {
        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, PARENT_ID))
                .andExpect(status().isNoContent());

        Optional<Reminder> changedReminder = reminderRepository.findById(PARENT_ID);

        assertTrue(changedReminder.isPresent());
        assertTrue(changedReminder.get().getCompleted());
    }

    @Test
    @Transactional
    @Rollback
    void changeCompleteStatusParentReminderWithChildrenSuccess() throws Exception {
        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, REMINDER_WITH_CHILDREN_ID))
                .andExpect(status().isNoContent());

        List<Reminder> changedReminders = reminderRepository.findReminderWithAllSiblings(REMINDER_WITH_CHILDREN_ID);

        for (Reminder reminder : changedReminders) {
            assertTrue(reminder.getCompleted());
        }
    }

    @Test
    @Transactional
    @Rollback
    void changeCompleteStatusReminderWithChildrenAndParentSuccess() throws Exception {
        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, REMINDER_WITH_CHILDREN_ID))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, REMINDER1_CHILD_ID))
                .andExpect(status().isNoContent());

        List<Reminder> changedReminders = reminderRepository.findReminderWithParents(REMINDER1_CHILD_ID);

        for (Reminder reminder : changedReminders) {
            assertFalse(reminder.getCompleted());
        }

        List<Reminder> unchangedChildren = reminderRepository.findReminderWithAllSiblings(REMINDER1_CHILD_ID);

        for (int i = 1; i < unchangedChildren.size(); i++) {
            assertTrue(unchangedChildren.get(i).getCompleted());
        }
    }

    @Test
    @Transactional
    @Rollback
    void changeCompleteStatusPeriodicReminderSuccess() throws Exception {
        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, PERIODIC_REMINDER_ID))
                .andExpect(status().isNoContent());

        Optional<Reminder> completedPeriodicReminder = reminderRepository.findById(PERIODIC_REMINDER_ID);

        assertTrue(completedPeriodicReminder.isPresent());
        assertTrue(completedPeriodicReminder.get().getCompleted());

        Optional<Reminder> newPeriodicReminder = reminderRepository.findById(SAVED_REMINDER_ID);

        assertTrue(newPeriodicReminder.isPresent());
        assertFalse(newPeriodicReminder.get().getCompleted());
        assertEquals(completedPeriodicReminder.get().getTitle(), newPeriodicReminder.get().getTitle());
        assertEquals(completedPeriodicReminder.get().getParent(), newPeriodicReminder.get().getParent());
        assertEquals(completedPeriodicReminder.get().getNestingDepth(), newPeriodicReminder.get().getNestingDepth());
        assertEquals(newPeriodicReminder.get().getExpireDate(), completedPeriodicReminder.get().getExpireDate().plusDays(30));
    }

    @Test
    void changeCompleteStatusNotFoundFail() throws Exception {
        mockMvc.perform(patch(REMINDERS_REST_URL_WITH_ID, NOT_FOUND_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Напоминание с id = " + NOT_FOUND_ID + " не найдено")));
    }
}


































