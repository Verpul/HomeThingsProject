package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.ReminderCategory;

public interface ReminderCategoryRepository extends JpaRepository<ReminderCategory, Long> {

    @Query("SELECT rc FROM ReminderCategory rc WHERE rc.title = :title")
    ReminderCategory findCategoryByTitle(String title);

}
