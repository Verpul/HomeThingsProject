-- populate weight_records
DELETE
FROM weight_records;
ALTER
SEQUENCE weight_records_id_seq RESTART WITH 1;

INSERT INTO weight_records (weight_record_date, weight_record_value)
VALUES ('2023-01-01', '62'),
       ('2023-02-01', '64.3'),
       ('2023-03-01', '67'),
       ('2023-04-01', '66.1'),
       ('2023-05-01', '65'),
       ('2023-06-01', '68'),
       ('2023-07-01', '68'),
       ('2023-08-01', '68.1');

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

INSERT INTO reminders (title, expire_date, remind_date, remind_time, category_id,
                       comment, parent_id, nesting_depth, periodic, period, periodicity, completed)
VALUES ('Напоминание без даты окончания', null, null, null, null, null, null, null, false, null, null, false),
       ('Напоминание с датой окончания', '2025-04-09', null, null, null, null, null, null, false, null, null, false),
       ('Напоминание с датой напоминания', '2025-04-09', '2023-07-06', null, null, 'Комментарий комментарий', null, null, false, null, null, false),
       ('Напоминание с датой и временем напоминания', '2025-04-09', '2023-06-06', '09:30:00', null, null, null, null, false, null, null, false),
       ('Напоминание с категорией 1', null, null, null, 1, null, null, null, false, null, null, false),
       ('Напоминание с категорией 2', null, null, null, 2, null, null, null, false, null, null, false),
       ('Напоминание с комментарием', '2024-04-09', null, null, null, 'Комментарий Комментарий Комментарий Комментарий ' ||
                                                                      'Комментарий Комментарий Комментарий Комментарий Комментарий', null, null, false, null, null, false),
       ('Напоминание с подзадачей', null, null, null, null, null, null, null, false, null, null, false),
       ('Напоминание второго уровня', null, null, null, null, null, 8, 1, false, null, null, false),
       ('Напоминание второго уровня с подзадачей', null, null, null, null, null, 8, 1, false, null, null, false),
       ('Напоминание третьего уровня c подзадачами', null, null, null, null, null, 10, 2, false, null, null, false),
       ('Напоминание четвертого уровня 1', null, null, null, null, null, 11, 3, false, null, null, false),
       ('Напоминание четвертого уровня 2', null, null, null, null, null, 11, 3, false, null, null, false),
       ('Напоминание пятого (последнего) уровня', null, null, null, null, null, 12, 4, false, null, null, false),
       ('Напоминание с периодичностью в 6 месяцев', '2025-01-01', null, null, null, null, null, null, true, 'MONTH', 6, false),
       ('Напоминание с периодичностью 1 год и напоминанием', '2025-01-01', '2024-06-06', null, null, null, null, null, true, 'YEAR', 1, false),
       ('Выполненное напоминание', null, null, null, null, null, null, null, false, null, null, true),
       ('Выполненное напоминание с категорией', null, null, null, 1, null, null, null, false, null, null, true);

-- populate currency
DELETE
FROM currency;
ALTER
SEQUENCE currency_id_seq RESTART WITH 1;

INSERT INTO currency (currency_from, from_amount, currency_to, to_amount, rate, exchange_date)
VALUES ('RUB', 1000, 'USD', 100, 10, '2020-01-01'),
    ('RUB', 2000, 'EUR', 100, 20, '2020-02-01');


