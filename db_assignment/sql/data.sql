USE library_db;

-- 🧑‍💻 Författare
INSERT INTO author (name, birth_date, nationality)
VALUES
    ('Astrid Lindgren', '1907-11-14', 'Sweden'),
    ('J.K. Rowling', '1965-07-31', 'UK'),
    ('J.R.R. Tolkien', '1892-01-03', 'UK'),
    ('Stephen King', '1947-09-21', 'USA'),
    ('Paulo Coelho', '1947-08-24', 'Brazil');

-- 📚 Böcker
INSERT INTO book (title, isbn, pub_year)
VALUES
    ('Pippi Långstrump', '9789129656769', 1945),
    ('Bröderna Lejonhjärta', '9789129656783', 1973),
    ('Harry Potter och Fenixorden', '9789129656771', 2003),
    ('Harry Potter och Halvblodsprinsen', '9789129656772', 2005),
    ('The Hobbit', '9780261102217', 1937),
    ('The Lord of the Rings', '9780261102385', 1954),
    ('The Shining', '9780307743657', 1977),
    ('It', '9780450411434', 1986),
    ('The Alchemist', '9780061122415', 1988),
    ('Veronika Decides to Die', '9780061015014', 1998);

-- Böcker & Författare
    INSERT INTO book_author (book_id, author_id)
    VALUES
        (1,1), -- Pippi - Astrid
        (2,1), -- Bröderna Lejonhjärta - Astrid
        (3,2), -- Fenixorden - J.K
        (4,2), -- Halvblodsprinsen - J.k
        (5,3),-- Hobbit - J.R.R
        (6,3), -- LOTR - J.R.R
        (7,4), -- The shining - Stephen
        (8,4), -- IT - Stephen
        (9,5), -- The Alchemist - Paulo
        (10,5); -- Veronika - Paulo

-- 👥 Medlemmar
INSERT INTO members (name)
VALUES
    ('Tobias Larsson'),
    ('Lisa Saari'),
    ('Dorotea Forslin'),
    ('Catherine Peralta');
