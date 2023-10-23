package ru.verpul.reminder;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.exception.AssociationFoundException;
import ru.verpul.exception.NotFoundException;
import ru.verpul.model.ReminderCategory;
import ru.verpul.repository.ReminderCategoryRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.verpul.reminder.ReminderCategoryTestData.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/devdata/schema.sql", "classpath:data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@AutoConfigureMockMvc
public class ReminderCategoryTest {
    private static final String CATEGORIES_REST_URL = "/api/reminder/category";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderCategoryRepository categoryRepository;

    @Test
    void getAllCategories() throws Exception {
        mockMvc.perform(get(CATEGORIES_REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Категория 1")));
    }

    @Test
    @Transactional
    @Rollback
    void createReminderCategorySuccess() throws Exception {
        ReminderCategoryDTO newCategory = getNewCategory();

        mockMvc.perform(post(CATEGORIES_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newCategory)))
                .andExpect(status().isCreated());

        ReminderCategory savedCategory = categoryRepository.findCategoryByTitle(newCategory.getTitle());

        assertNotNull(savedCategory);
        assertEquals(savedCategory.getTitle(), newCategory.getTitle());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void createReminderCategoryValidationNotBlank(String title) throws Exception {
        ReminderCategoryDTO category = getNewCategory();
        category.setTitle(title);

        mockMvc.perform(post(CATEGORIES_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Наименование категории должно быть заполнено")));
    }

    @Test
    void createReminderCategoryValidationMaxSize() throws Exception {
        ReminderCategoryDTO category = getNewCategory();
        category.setTitle("Категория".repeat(50));

        mockMvc.perform(post(CATEGORIES_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(jsonPath("$.title", is("Название категории не должно превышать 100 символов")));
    }

    @Test
    void createReminderCategoryValidationUnique() throws Exception {
        ReminderCategoryDTO category = getNewCategory();
        category.setTitle(FIRST_CATEGORY_TITLE);

        mockMvc.perform(post(CATEGORIES_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(jsonPath("$.title", is("Категория должна быть уникальной")));
    }

    @Test
    void updateReminderCategorySuccess() throws Exception {
        ReminderCategoryDTO category = getUpdatedCategory();

        mockMvc.perform(put(CATEGORIES_REST_URL + "/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isNoContent());

        ReminderCategory oldCategory = categoryRepository.findCategoryByTitle(FIRST_CATEGORY_TITLE);
        ReminderCategory savedCategory = categoryRepository.findCategoryByTitle(category.getTitle());

        assertNull(oldCategory);
        assertNotNull(savedCategory);
        assertEquals(savedCategory.getTitle(), category.getTitle());
    }

    @Test
    void updateReminderCategoryValidationUniqueSuccess() throws Exception {
        ReminderCategoryDTO category = getUpdatedCategory();
        category.setTitle(FIRST_CATEGORY_TITLE);

        mockMvc.perform(put(CATEGORIES_REST_URL + "/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateReminderCategoryNotFound() throws Exception {
        ReminderCategoryDTO category = getUpdatedCategory();
        category.setId(NOT_FOUND_ID);

        mockMvc.perform(put(CATEGORIES_REST_URL + "/" + NOT_FOUND_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(jsonPath("$", is("Категория с id = " + NOT_FOUND_ID + " не найдена")));
    }

    @Test
    @Transactional
    @Rollback
    void deleteReminderCategorySuccess() throws Exception {
        mockMvc.perform(delete(CATEGORIES_REST_URL + "/" + FIRST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<ReminderCategory> deletedCategory = categoryRepository.findById(FIRST_CATEGORY_ID);

        assertEquals(Optional.empty(), deletedCategory);
    }

    @Test
    void deleteReminderCategoryAssociationsExists() throws Exception {
        mockMvc.perform(delete(CATEGORIES_REST_URL + "/" + CATEGORY_WITH_REMINDER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AssociationFoundException))
                .andExpect(jsonPath("$", is("У этой категории существуют напоминания: " + 2 + " шт., удаление невозможно")));
    }

    @Test
    void deleteReminderCategoryNotFound() throws Exception {
        mockMvc.perform(delete(CATEGORIES_REST_URL + "/" + NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}




























