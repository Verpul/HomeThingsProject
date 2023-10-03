package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verpul.model.ReminderCategory;

public interface ReminderCategoryRepository extends JpaRepository<ReminderCategory, Long> {
}
