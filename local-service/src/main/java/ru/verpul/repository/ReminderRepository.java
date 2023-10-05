package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.Reminder;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    @Query("SELECT r FROM  Reminder r ORDER BY r.parentId DESC, r.expireDate, r.id")
    List<Reminder> findAllOrderByParentIdAndExpireDate();

    @Query("SELECT r FROM Reminder r WHERE r.category.id = :category")
    List<Reminder> findByCategory(Long category);
}
