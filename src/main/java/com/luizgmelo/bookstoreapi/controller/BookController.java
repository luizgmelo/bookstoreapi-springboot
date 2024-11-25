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
import java.util.Optional;

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
    public ResponseEntity<Book> getOneBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOpt = bookService.getBookById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }

        throw new BookNotFoundException();
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

        throw new BookNotFoundException();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOpt = bookService.getBookById(id);

        if (bookOpt.isPresent()) {
            bookService.deleteBookById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new BookNotFoundException();
    }
}
