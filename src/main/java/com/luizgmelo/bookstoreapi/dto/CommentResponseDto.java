package com.luizgmelo.bookstoreapi.dto;

import com.luizgmelo.bookstoreapi.model.User;

import java.util.Date;

public record CommentResponseDto(String comment, User author, Date createAt) {
}
