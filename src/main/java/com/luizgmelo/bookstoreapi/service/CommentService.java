package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.CommentDto;
import com.luizgmelo.bookstoreapi.exceptions.CommentNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Comment;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    public Comment createComment(CommentDto dto, User author, Book book) {
        Comment comment = new Comment();
        comment.setCommentText(dto.commentText());
        comment.setBook(book);
        comment.setAuthor(author);
        comment.setCreateAt(dto.createdAt());

        return commentRepository.save(comment);
    }

    public Page<Comment> getCommentsByBook(Book book, Pageable pageable) {
        return commentRepository.findByBook(book, pageable);
    }

    public void deleteComment(Long id) {
        getCommentById(id);
        commentRepository.deleteById(id);
    }
}
