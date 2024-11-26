package com.luizgmelo.bookstoreapi.constants;

import com.luizgmelo.bookstoreapi.model.Book;

import java.time.LocalDate;

public class Constants {
    public static final Book BOOK_0 = new Book(1, "The art of clean code", "Robert Cleanman", "CodeMastrePress", LocalDate.of(2020,6,10), "Tecnologia", "Inglês", 420);
    public static final Book BOOK_1 = new Book(2, "A Jornada do Herói", "Clara Aventura", "Narrativa Viva", LocalDate.of(2018,8,22), "Fantasia", "Português", 320);
}
