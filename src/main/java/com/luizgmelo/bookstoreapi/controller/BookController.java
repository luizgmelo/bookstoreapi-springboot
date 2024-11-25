package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOneBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOpt = bookService.getBookById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Integer id, @RequestBody BookDto dto) {
        Optional<Book> bookUpdatedOpt =  bookService.updateBook(id, dto);

        if (bookUpdatedOpt.isPresent()) {
            Book bookUpdated = bookUpdatedOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(bookUpdated);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Integer id) {
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
