package com.luizgmelo.bookstoreapi.dto;

import com.luizgmelo.bookstoreapi.model.Loan;

import java.util.List;

public class LoanMapper {
    public static LoanDto toDTO(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getBook().getBookId(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getUser().getUsernameField(),
                loan.getFine(),
                loan.getReturned()
        );
    }

    public static List<LoanDto> toDTOList(List<Loan> loans) {
        return loans.stream().map(LoanMapper::toDTO).toList();
    }
}
