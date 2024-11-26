package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.exceptions.BookAlreadyExistsException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<Book> getBookById(Integer id) {
        return repository.findById(id);
    }

    public Book createBook(BookDto dto) {
        boolean exists = repository.existsByTitle(dto.title());

        if (exists) {
            throw new BookAlreadyExistsException("A boot with title " + dto.title() + " already exists.");
        }

        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        return repository.save(book);
    }

    public Optional<Book> updateBook(Integer id, BookDto dto) {
        Optional<Book> bookOpt = repository.findById(id);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            BeanUtils.copyProperties(dto, book);
            return Optional.of(repository.save(book));
        }

        return Optional.empty();
    }

    public void deleteBookById(Integer id) {
        repository.deleteById(id);
    }
}
