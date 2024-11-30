package com.luizgmelo.bookstoreapi.dto;

import java.time.LocalDate;

public record LoanDto(Long id, Integer bookId, LocalDate startDate, LocalDate endDate, String username, Double fine, Boolean returned)  {
}
