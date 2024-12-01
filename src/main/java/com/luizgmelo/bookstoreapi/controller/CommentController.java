package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.CommentDto;
import com.luizgmelo.bookstoreapi.dto.CommentMapper;
import com.luizgmelo.bookstoreapi.dto.CommentResponseDto;
import com.luizgmelo.bookstoreapi.exceptions.BookNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Comment;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import com.luizgmelo.bookstoreapi.service.BookService;
import com.luizgmelo.bookstoreapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/books/{id}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Integer id, @RequestBody CommentDto commentDto) {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new RuntimeException("Failed read authentication details.");
        }
        
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        Comment comment = commentService.createComment(commentDto, authenticatedUser, book);
        CommentResponseDto response = new CommentResponseDto(comment.getCommentText(), comment.getAuthor(), comment.getCreateAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books/{id}/comments")
    public ResponseEntity<List<CommentDto>> listBookComments(@PathVariable Integer id, Pageable pageable) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Page<Comment> comments = commentService.getCommentsByBook(book, pageable);
        List<CommentDto> response = CommentMapper.toDTOList(comments.getContent());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/books/{bookId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "bookId") Integer bookId, @PathVariable(value = "commentId") Long commentId) {
        UserDetails authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new RuntimeException("Failed read authentication details.");
        }

        bookService.getBookById(bookId);
        Comment comment = commentService.getCommentById(commentId);

        if (!authenticatedUser.getUsername().equals(comment.getAuthor().getUsernameField())) {
            throw new RuntimeException("Unauthorized process");
        }

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
