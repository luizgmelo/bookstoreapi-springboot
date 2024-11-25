package com.luizgmelo.bookstoreapi;

import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookstoreapiApplicationTests {
	static final Book BOOK_0 = new Book(1, "The art of clean code", "Robert Cleanman", "CodeMastrePress", LocalDate.of(2020,6,10), "Tecnologia", "Inglês", 420);
	static final Book BOOK_1 = new Book(2, "A Jornada do Herói", "Clara Aventura", "Narrativa Viva", LocalDate.of(2018,8,22), "Fantasia", "Português", 320);

	@Autowired
	BookService bookService;

	@Test
	void testGetBooksPagination() {
		Pageable pageable = PageRequest.of(0, 2);

		Page<Book> booksPage = bookService.getBooks(pageable);

		assertThat(booksPage.getContent()).hasSize(2);
		assertThat(BOOK_0.getTitle()).isEqualTo(booksPage.getContent().get(0).getTitle());
		assertThat(BOOK_1).isEqualTo(booksPage.getContent().get(1));
	}

}
