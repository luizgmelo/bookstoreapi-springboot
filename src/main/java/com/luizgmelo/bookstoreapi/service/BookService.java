package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.exceptions.BookAlreadyExistsException;
import com.luizgmelo.bookstoreapi.exceptions.BookNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Page<Book> getBooks(Pageable pageable, String title, String author) {
        if (title != null && author != null) {
            return repository.findBookByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author, pageable);
        } else if (title != null) {
            return repository.findBookByTitleContainingIgnoreCase(title, pageable);
        } else if (author != null) {
            return repository.findBookByAuthorContainingIgnoreCase(author, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Book getBookById(Integer id) {
        return repository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public Book createBook(BookDto dto) {
        boolean exists = repository.existsByTitle(dto.title());

        if (exists) {
            throw new BookAlreadyExistsException(dto.title());
        }

        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        return repository.save(book);
    }

    public Book updateBook(Integer id, BookDto dto) {
        Book book = repository.findById(id).orElseThrow(BookNotFoundException::new);

        boolean exists = repository.existsByTitleAndBookIdNot(dto.title(), id);

        if (exists) {
            throw new BookAlreadyExistsException(dto.title());
        }

        BeanUtils.copyProperties(dto, book);
        return repository.save(book);
    }

    public Book deleteBookById(Integer id) {
        Book removed = repository.findById(id).orElseThrow(BookNotFoundException::new);
        repository.deleteById(id);
        return removed;
    }
}
