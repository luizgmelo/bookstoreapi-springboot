package com.luizgmelo.bookstoreapi.repository;

import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findAllByBook(Book book, Pageable pageable);
}
