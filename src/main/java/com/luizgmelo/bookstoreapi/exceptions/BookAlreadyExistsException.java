package com.luizgmelo.bookstoreapi.exceptions;

public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException() {
        super("This book already exists.");
    }

    public BookAlreadyExistsException(String title) {
        super("A boot with title " + title + " already exists.");
    }
}
