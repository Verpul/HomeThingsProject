package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verpul.model.Reminder;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
