-- populate reminders
DELETE
FROM reminders;
ALTER
    SEQUENCE reminders_id_seq RESTART WITH 1;

DELETE
FROM reminder_category;
ALTER
SEQUENCE reminder_category_id_seq RESTART WITH 1;

INSERT INTO reminder_category (title)
VALUES ('Категория 1'),
       ('Категория 2');

-- Категория 1 без напоминаний
-- Категория 2 одно напоминине

INSERT INTO reminders (title, expire_date, remind_date, remind_time, category_id,
                       comment, parent_id, nesting_depth, periodic, period, periodicity, completed)
VALUES ('Напоминание со второй категорией', null, null, null, 2, null, null, null, false, null, null, false);
--        ('Напоминание с датой окончания', '2025-04-09', null, null, null, null, null, null, false, null, null, false),
--        ('Напоминание с датой напоминания', '2025-04-09', '2025-06-06', null, null, null, null, null, false, null, null, false),
--        ('Напоминание с датой и временем напоминания', '2025-04-09', '2023-06-06', '09:30:00', null, null, null, null, false, null, null, false),
--        ('Напоминание с категорией 1', null, null, null, 1, null, null, null, false, null, null, false),
--        ('Напоминание с категорией 2', null, null, null, 2, null, null, null, false, null, null, false),
--        ('Напоминание с комментарием', '2024-04-09', null, null, null, 'Комментарий Комментарий Комментарий Комментарий ' ||
--                                                                       'Комментарий Комментарий Комментарий Комментарий Комментарий', null, null, false, null, null, false),
--        ('Напоминание с подзадачей', null, null, null, null, null, null, null, false, null, null, false),
--        ('Напоминание второго уровня', null, null, null, null, null, 8, 1, false, null, null, false),
--        ('Напоминание второго уровня с подзадачей', null, null, null, null, null, 8, 1, false, null, null, false),
--        ('Напоминание третьего уровня c подзадачами', null, null, null, null, null, 10, 2, false, null, null, false),
--        ('Напоминание четвертого уровня 1', null, null, null, null, null, 11, 3, false, null, null, false),
--        ('Напоминание четвертого уровня 2', null, null, null, null, null, 11, 3, false, null, null, false),
--        ('Напоминание пятого (последнего) уровня', null, null, null, null, null, 12, 4, false, null, null, false),
--        ('Напоминание с периодичностью в 6 месяцев', '2025-01-01', null, null, null, null, null, null, true, 'MONTH', 6, false),
--        ('Напоминание с периодичностью 1 год и напоминанием', '2025-01-01', '2024-06-06', null, null, null, null, null, true, 'YEAR', 1, false),
--        ('Выполненное напоминание', null, null, null, null, null, null, null, false, null, null, true),
--        ('Выполненное напоминание с категорией', null, null, null, 1, null, null, null, false, null, null, true);


