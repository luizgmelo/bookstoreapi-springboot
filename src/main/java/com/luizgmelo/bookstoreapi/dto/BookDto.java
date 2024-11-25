package com.luizgmelo.bookstoreapi.dto;

import java.time.LocalDate;

public record BookDto(String title, String author, String publisher, LocalDate publicationDate,
                      String genre, String language, Integer pageCount) {
}
