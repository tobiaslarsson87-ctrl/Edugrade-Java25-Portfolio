CREATE DATABASE IF NOT EXISTS library_db;

USE library_db;

CREATE TABLE IF NOT EXISTS author
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    birth_date  DATE,
    nationality VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS book
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(200) NOT NULL,
    isbn     VARCHAR(30) UNIQUE,
    pub_year INT
);
CREATE TABLE IF NOT EXISTS book_author
(
    book_id   int NOT NULL,
    author_id int NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (author_id) REFERENCES author (id)

);

CREATE TABLE IF NOT EXISTS members
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS loan
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    book_isbn   VARCHAR(30),
    renter      INT,
    loan_date   DATE,
    return_date DATE,
    FOREIGN KEY (book_isbn) REFERENCES book (isbn),
    FOREIGN KEY (renter) REFERENCES members (id)
);


SHOW TABLES;

