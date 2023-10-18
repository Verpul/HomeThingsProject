package ru.verpul.repository;

import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    @Query("SELECT r FROM  Reminder r ORDER BY r.completed, r.parent.id DESC, r.expireDate, r.id")
    List<Reminder> findAllOrderByParentIdAndExpireDate();

    @Query("SELECT r FROM Reminder r WHERE r.category.id = :category ORDER BY r.completed, r.parent.id DESC, r.expireDate, r.id")
    List<Reminder> findAllByCategory(Long category);

    @Query("SELECT r FROM  Reminder r WHERE r.completed = false ORDER BY r.completed, r.parent.id DESC, r.expireDate, r.id")
    List<Reminder> findUncompletedOrderByParentIdAndExpireDate();

    @Query("SELECT r FROM Reminder r WHERE r.category.id = :category AND r.completed = false ORDER BY r.completed, r.parent.id DESC, r.expireDate, r.id")
    List<Reminder> findUncompletedByCategory(Long category);

    @Query(value = "WITH RECURSIVE reminders_tree AS ( " +
            "SELECT * FROM reminders WHERE id = :id " +
            "UNION ALL " +
            "SELECT r.* FROM reminders r " +
            "JOIN reminders_tree rt ON r.parent_id = rt.id) " +
            "SELECT * FROM reminders_tree", nativeQuery = true)
    List<Reminder> findReminderWithAllSiblings(Long id);

    @Query(value = "WITH RECURSIVE reminders_tree AS ( " +
            "SELECT * FROM reminders WHERE id = :id " +
            "UNION ALL " +
            "SELECT r.* FROM reminders r " +
            "JOIN reminders_tree rt ON r.id = rt.parent_id) " +
            "SELECT * FROM reminders_tree", nativeQuery = true)
    List<Reminder> findReminderWithParents(Long id);

    @Query(value = "SELECT * FROM reminders r " +
            "WHERE r.remind_date <= NOW() " +
            "AND (r.remind_time <= CAST(CURRENT_DATE || ' ' || CURRENT_TIME AS time) OR r.remind_time IS NULL) " +
            "AND r.completed = false " +
            "ORDER BY r.expire_date", nativeQuery = true)
    List<Reminder> getReminderWithRemindDateAndTimeLessThanNow();

    @Query(value = "SELECT * FROM reminders r " +
            "WHERE r.remind_date = CURRENT_DATE " +
            "AND r.remind_time IS NOT NULL " +
            "AND r.completed = false", nativeQuery = true)
    List<Reminder> getRemindersForTodayWithRemindTime();
}
