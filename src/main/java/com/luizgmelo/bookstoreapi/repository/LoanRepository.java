package com.luizgmelo.bookstoreapi.repository;

import com.luizgmelo.bookstoreapi.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
//    Page<Loan> findByEndDateAndReturnedFalse(LocalDate date, Pageable pageable);

    Page<Loan> findByEndDateBeforeAndReturnedIsFalse(LocalDate endDateBefore, Pageable pageable);
}
