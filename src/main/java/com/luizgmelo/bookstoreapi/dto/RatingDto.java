package com.luizgmelo.bookstoreapi.dto;

import com.luizgmelo.bookstoreapi.model.Book;

import java.util.Date;

public record RatingDto(Double rating, Date createdAt, Book book, String author) {
}
