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

INSERT INTO reminders (title, expire_date, remind_date, remind_time)
VALUES ('Напоминание без даты окончания', null, null, null),
       ('Напоминание с датой окончания', '2023-04-09', null, null),
       ('Напоминание с датой напоминания', '2023-04-09', '2023-06-06', null),
       ('Напоминание с датой и временем напоминания', '2023-04-09', '2023-06-06', '09:30:00')

