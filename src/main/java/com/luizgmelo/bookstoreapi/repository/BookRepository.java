package com.luizgmelo.bookstoreapi.repository;

import com.luizgmelo.bookstoreapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findBookByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    Page<Book> findBookByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findBookByAuthorContainingIgnoreCase(String author, Pageable pageable);

}
