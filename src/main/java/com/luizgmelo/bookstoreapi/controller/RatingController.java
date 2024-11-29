package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.RatingDto;
import com.luizgmelo.bookstoreapi.dto.RatingMapper;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Rating;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.service.BookService;
import com.luizgmelo.bookstoreapi.service.RatingService;
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
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private BookService bookService;

    @PostMapping("/books/{id}/ratings")
    public ResponseEntity<RatingDto> createRating(@PathVariable Integer id, @RequestBody RatingDto dto) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read authentication details");
        }

        Book book = bookService.getBookById(id);

        Rating newRating = ratingService.createRating(dto, book, user);
        RatingDto response = RatingMapper.toDTO(newRating);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books/{id}/ratings")
    public ResponseEntity<List<RatingDto>> listBookRatings(@PathVariable Integer id, Pageable pageable) {
        Book book = bookService.getBookById(id);

        Page<Rating> ratings = ratingService.getRatingsByBook(book, pageable);
        List<RatingDto> response = RatingMapper.toDTOList(ratings.getContent());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/books/{bookId}/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable(value = "bookId") Integer bookId,
                                             @PathVariable(value = "ratingId") Long ratingId) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read authentication details");
        }

        bookService.getBookById(bookId);

        Rating rating = ratingService.getRatingById(ratingId);

        UserDetails ratingAuthor = rating.getAuthor();
        if (!ratingAuthor.getUsername().equals(user.getUsername())) {
            // <TODO> Threat Operation not allowed exception
            throw new RuntimeException("Operation not allowed");
        }

        ratingService.deleteRating(ratingId);
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

