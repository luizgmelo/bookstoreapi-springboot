package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.exceptions.BookNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks(pageable).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOneBook(@PathVariable Integer id) {
       return bookService.getBookById(id)
               .map(book -> ResponseEntity.status(HttpStatus.OK).body(book))
               .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody BookDto dto) {
        return bookService.updateBook(id, dto)
                .map(book -> ResponseEntity.status(HttpStatus.OK).body(book))
                .orElseThrow(BookNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Integer id) {
        return bookService.getBookById(id)
                .map(book -> {
                    bookService.deleteBookById(id);
                    return ResponseEntity.status(HttpStatus.OK).body(book);
                }).orElseThrow(BookNotFoundException::new);
    }
}
