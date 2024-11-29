package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.RatingDto;
import com.luizgmelo.bookstoreapi.exceptions.RatingNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Rating;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id).orElseThrow(RatingNotFoundException::new);
    }

    public Rating createRating(RatingDto dto, Book book, User author) {
        Rating newRating = new Rating();
        newRating.setRating(dto.rating());
        newRating.setBook(book);
        newRating.setAuthor(author);

        return ratingRepository.save(newRating);
    }

    public Page<Rating> getRatingsByBook(Book book, Pageable pageable) {
        return ratingRepository.findAllByBook(book, pageable);
    }

    public void deleteRating(Long id) {
        getRatingById(id);
        ratingRepository.deleteById(id);
    }
}
