package com.luizgmelo.bookstoreapi.constants;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.model.Book;

import java.time.LocalDate;

public class Constants {
    public static final Book BOOK_0 = new Book(1, "The art of clean code", "Robert Cleanman", "CodeMastrePress", LocalDate.of(2020,6,10), "Tecnologia", "Inglês", 420);
    public static final Book BOOK_1 = new Book(2, "A Jornada do Herói", "Clara Aventura", "Narrativa Viva", LocalDate.of(2018,8,22), "Fantasia", "Português", 320);
    public static final BookDto BOOK_0_DTO = new BookDto(BOOK_0.getTitle(), BOOK_0.getAuthor(), BOOK_0.getPublisher(), BOOK_0.getPublicationDate(), BOOK_0.getGenre(), BOOK_0.getLanguage(), BOOK_0.getPageCount());
}
