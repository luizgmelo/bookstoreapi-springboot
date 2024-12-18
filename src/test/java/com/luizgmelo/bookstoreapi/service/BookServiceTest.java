package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.exceptions.BookAlreadyExistsException;
import com.luizgmelo.bookstoreapi.exceptions.BookNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

import static com.luizgmelo.bookstoreapi.constants.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Should return a page with two books that are in database")
    void testGetBooksPagination() {
        List<Book> books = List.of(BOOK_0, BOOK_1);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> mockPage = new PageImpl<>(books, pageable, books.size());
        when(repository.findAll(pageable)).thenReturn(mockPage);

        Page<Book> booksPage = bookService.getBooks(pageable, null, null);

        assertThat(booksPage.getContent()).hasSize(2);
        assertThat(BOOK_0).isEqualTo(booksPage.getContent().get(0));
        assertThat(BOOK_1).isEqualTo(booksPage.getContent().get(1));
    }

    @Test
    @DisplayName("Should return a Page of books filter by title")
    void getBooksCase1() {
        List<Book> books = List.of(BOOK_0);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> mockPage = new PageImpl<>(books, pageable, books.size());
        when(repository.findBookByTitleContainingIgnoreCase(BOOK_0.getTitle(), pageable)).thenReturn(mockPage);

        Page<Book> sut = bookService.getBooks(pageable, BOOK_0.getTitle(), null);

        assertThat(sut).isNotEmpty();
        assertThat(sut.getContent()).hasSize(1);
        assertThat(sut.getContent()).containsExactly(BOOK_0);
    }

    @Test
    @DisplayName("Should return a Page of books filter by author")
    void getBooksCase2() {
        List<Book> books = List.of(BOOK_1);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> mockPage = new PageImpl<>(books, pageable, books.size());
        when(repository.findBookByAuthorContainingIgnoreCase(BOOK_1.getAuthor(), pageable)).thenReturn(mockPage);

        Page<Book> sut = bookService.getBooks(pageable, null, BOOK_1.getAuthor());

        assertThat(sut.getContent()).isNotEmpty();
        assertThat(sut.getContent()).hasSize(1);
        assertThat(sut.getContent()).containsExactly(BOOK_1);
    }


    @Test
    @DisplayName("Should return a Book if exists in database")
    void getBookByIdCase1() {
        when(repository.findById(any())).thenReturn(Optional.of(BOOK_0));

        Book sut = bookService.getBookById(1);

        assertThat(sut).isEqualTo(BOOK_0);
    }

    @Test
    @DisplayName("Should not return a Book if do not exists in database")
    void getBookByIdCase2() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookById(9999));

        assertThat(exception.getMessage()).isEqualTo("Book not found.");
    }

    @Test
    @DisplayName("Should create a new Book in database")
    void createBookCase1() {
        when(repository.save(any(Book.class))).thenReturn(BOOK_0);

        Book sut = bookService.createBook(BOOK_0_DTO);

        assertThat(sut).isEqualTo(BOOK_0);
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should thorw exception when create a Book already exists in database")
    void createBookCase2() {

        when(repository.existsByTitle(BOOK_0_DTO.title())).thenReturn(true);

        BookAlreadyExistsException exception = assertThrows(
                BookAlreadyExistsException.class,
                () -> bookService.createBook(BOOK_0_DTO)
        );

        assertThat(exception.getMessage())
                .isEqualTo("A boot with title " + BOOK_0_DTO.title() + " already exists.");

        verify(repository, times(1)).existsByTitle(BOOK_0_DTO.title());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should update a book in database")
    void updateBookCase1() {
        when(repository.findById(any())).thenReturn(Optional.of(BOOK_0));
        when(repository.save(any(Book.class))).thenReturn(BOOK_0);

        Book sut = bookService.updateBook(1, BOOK_0_DTO);

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(BOOK_0);
    }

    @Test
    @DisplayName("Should throw exception if update a book for a title that already exists in database")
    void updateBookCase2() {
        when(repository.findById(any())).thenReturn(Optional.of(BOOK_1));
        when(repository.existsByTitleAndBookIdNot(any(), any())).thenReturn(true);

        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class, () -> bookService.updateBook(2, BOOK_0_DTO));

        assertThat(exception.getMessage()).isEqualTo("A boot with title " + BOOK_0_DTO.title() + " already exists.");
    }

    @Test
    @DisplayName("Should delete a book if exists in database")
    public void deleteBookByIdCase1() {
        int id = 1;
        when(repository.findById(any())).thenReturn(Optional.of(BOOK_0));
        doNothing().when(repository).deleteById(any());

        Book removed = bookService.deleteBookById(id);

        assertThat(removed).isEqualTo(BOOK_0);

        verify(repository, times(id)).deleteById(id);
    }

    @Test
    @DisplayName("Should throws not found exception if delete a book that not exists in database")
    public void deleteBookByIdCase2() {
        int id = 1;
        when(repository.findById(any())).thenReturn(Optional.empty());
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,() -> bookService.deleteBookById(id));
        assertThat(exception.getMessage()).isEqualTo("Book not found.");
    }
}