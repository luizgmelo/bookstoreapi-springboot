package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.service.BookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(Pageable pageable,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) String author) {
        List<Book> books = bookService.getBooks(pageable, title, author).getContent();
        for (Book book : books) {
            int id = book.getBookId();
            book.add(linkTo(methodOn(BookController.class).getOneBook(id)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOneBook(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        book.add(linkTo(methodOn(BookController.class).getBooks(PageRequest.of(0, 1), null, null)).withRel("List of books."));
       return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody BookDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBookById(id));

    }
}
