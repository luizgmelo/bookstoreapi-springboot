package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOneBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOpt = repository.findById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        repository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Integer id, @RequestBody BookDto dto) {
        Optional<Book> bookOpt =  repository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            BeanUtils.copyProperties(dto, book);
            repository.save(book);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Integer id) {
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
