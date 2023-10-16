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
VALUES ('Напоминание со второй категорией', null, null, null, 2, null, null, null, false, null, null, false),
       ('Напоминание законченное', null, null, null, null, null, null, null, false, null, null, true),
       ('Напоминание незаконченное', null, null, null, null, null, null, null, false, null, null, false),
       ('Напоминание со второй категорией законченное', null, null, null, 2, null, null, null, false, null, null, true),
       ('Напоминине с датой напоминания', '2025-01-01', '2025-01-01', null, null, null, null, null, false, null, null, false),
       ('Напоминине с датой и временем напоминания', '2026-01-01', '2026-01-01', '21:30:00', null, null, null, null, false, null, null, false),
       ('Напоминине с поднапоминаниями', '2024-01-01', '2024-01-01', null, null, null, null, null, false, null, null, false),
       ('Поднапоминание 2 уровня 1', null, null, null, null, null, 7, 1, false, null, null, false),
       ('Поднапоминание 3 уровня', null, null, null, null, null, 8, 2, false, null, null, false),
       ('Поднапоминание 4 уровня', null, null, null, null, null, 9, 3, false, null, null, false),
       ('Поднапоминание 5 уровня', null, null, null, null, null, 10, 4, false, null, null, false),
       ('Поднапоминание 2 уровня 2', null, null, null, null, null, 7, 1, false, null, null, false),
       ('Поднапоминание 3 уровня законченное', null, null, null, null, null, 12, 2, false, null, null, true),
       ('Напоминине периодическое', '2026-01-01', null, null, null, null, null, null, true, 'DAY', 30, false),
       ('Поднапоминание 4 уровня периодическое', '2026-01-01', null, null, null, null, 9, 3, true, 'WEEK', 2, false);

-- 15 всего, 12 незаконченных, 2 с категорией





