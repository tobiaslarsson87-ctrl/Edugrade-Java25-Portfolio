-- reset tables
DELETE FROM gym_class;
DELETE FROM app_user;
DELETE FROM booking;

ALTER TABLE gym_class ALTER COLUMN id RESTART WITH 1;
ALTER TABLE app_user ALTER COLUMN id RESTART WITH 1;
ALTER TABLE booking ALTER COLUMN id RESTART WITH 1;

-- same seed data with refreshed tables
INSERT INTO app_user (username, password, role) VALUES
    ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN');

-- Seed classes - MIN: 3
INSERT INTO gym_class (name, instructor, description, day_of_week, start_time, duration_minutes, max_participants)
VALUES ('Boxing', 'Anna', 'Learn to punch!', 'Wednesday', '17:30', 90, 25),
       ('Yoga', 'Karin', 'Learn to relax!', 'Monday', '14:30', 60, 15),
       ('Powerlifting', 'Jonas', 'Get ripped!', 'Wednesday', '19:00', 120, 10);

-- Seed Bookings - MIN: 6 - bookings as far as I understand should not have join tables with users, it's just an access limiter (Only Admin/User can book)
-- Names, E-mails and timestamps are AI generated.
INSERT INTO booking (gym_class_id, participant_name, e_mail, booked_at)
VALUES
    (1, 'Sara Lind', 'sara.lind@example.com', TIMESTAMP '2026-04-01 09:00:00'),
    (1, 'Marcus Holm', 'marcus.holm@example.com', TIMESTAMP '2026-04-01 10:00:00'),
    (2, 'Elin Berg', 'elin.berg@example.com', TIMESTAMP '2026-04-01 11:00:00'),
    (2, 'Jonas Ek', 'jonas.ek@example.com', TIMESTAMP '2026-04-01 12:00:00'),
    (3, 'Nora Dahl', 'nora.dahl@example.com', TIMESTAMP '2026-04-02 09:00:00'),
    (3, 'Oskar Sand', 'oskar.sand@example.com', TIMESTAMP '2026-04-02 12:00:00');