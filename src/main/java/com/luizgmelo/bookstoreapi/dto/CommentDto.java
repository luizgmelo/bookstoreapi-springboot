package com.luizgmelo.bookstoreapi.dto;

import java.util.Date;

public record CommentDto (String commentText, Date createdAt, String author){
}
