package com.luizgmelo.bookstoreapi.dto;

import com.luizgmelo.bookstoreapi.model.Rating;

import java.util.List;

public class RatingMapper {
    public static RatingDto toDTO(Rating rating) {
        return new RatingDto(
                rating.getRating(),
                rating.getCreatedAt(),
                rating.getBook(),
                rating.getAuthor().getUsernameField()
        );
    }

    public static List<RatingDto> toDTOList(List<Rating> ratings) {
        return ratings.stream().map(RatingMapper::toDTO).toList();
    }
}
