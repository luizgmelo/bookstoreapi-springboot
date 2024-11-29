package com.luizgmelo.bookstoreapi.dto;

import java.util.List;

import com.luizgmelo.bookstoreapi.model.Comment;

public class CommentMapper {
    public static CommentDto toDTO(Comment comment) {
        return new CommentDto(
                comment.getCommentText(),
                comment.getCreateAt(),
                comment.getAuthor().getUsernameField()
        );
    }

    public static List<CommentDto> toDTOList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDTO).toList();
    }
}
