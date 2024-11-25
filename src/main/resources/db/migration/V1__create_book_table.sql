CREATE TABLE books(
    book_id SERIAL PRIMARY KEY ,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    publication_date DATE,
    genre VARCHAR(50),
    language VARCHAR (50) DEFAULT 'English',
    page_count INT CHECK (page_count > 0)
);