package com.luizgmelo.bookstoreapi.exceptions;

public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException() {
        super("Rating not found.");
    }

    public RatingNotFoundException(String message) {
        super(message);
    }
}
