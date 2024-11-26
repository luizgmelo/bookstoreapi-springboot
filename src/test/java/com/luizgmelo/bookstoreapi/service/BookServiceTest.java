package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.luizgmelo.bookstoreapi.constants.Constants.BOOK_0;
import static com.luizgmelo.bookstoreapi.constants.Constants.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void testGetBooksPagination() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<Book> booksPage = bookService.getBooks(pageable, null, null);

        assertThat(booksPage.getContent()).hasSize(2);
        assertThat(BOOK_0.getTitle()).isEqualTo(booksPage.getContent().get(0).getTitle());
        assertThat(BOOK_1).isEqualTo(booksPage.getContent().get(1));
    }

    @Test
    @DisplayName("Should return a Page of books filter by title")
    void getBooksCase1() {
        Page<Book> sut = bookService.getBooks(PageRequest.of(0, 2), BOOK_0.getTitle(), null);

        assertThat(sut).isNotEmpty();
        assertThat(sut.getContent()).hasSize(1);
        assertThat(sut.getContent()).containsExactly(BOOK_0);
    }

    @Test
    @DisplayName("Should return a Page of books filter by author")
    void getBooksCase2() {
        Page<Book> sut = bookService.getBooks(PageRequest.of(0, 2), null, BOOK_1.getAuthor());

        assertThat(sut.getContent()).isNotEmpty();
        assertThat(sut.getContent()).hasSize(1);
        assertThat(sut.getContent()).containsExactly(BOOK_1);
    }


    @Test
    @DisplayName("Should return a Book if exists in database")
    void getBookByIdCase1() {

        Optional<Book> sut = bookService.getBookById(1);

        assertThat(sut).isNotEmpty();
        assertEquals(BOOK_0, sut.get());
    }

    @Test
    @DisplayName("Should not return a Book if do not exists in database")
    void getBookByIdCase2() {
        Optional<Book> sut = bookService.getBookById(9999);

        assertThat(sut).isEmpty();
    }
}