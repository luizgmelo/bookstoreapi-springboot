package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.BookDto;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Optional<Book> getBookById(Integer id) {
        return repository.findById(id);
    }

    public Book createBook(BookDto dto) {
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
