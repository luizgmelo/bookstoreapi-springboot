package com.luizgmelo.bookstoreapi.exceptions;

public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException() {
        super("This book already exists.");
    }

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
