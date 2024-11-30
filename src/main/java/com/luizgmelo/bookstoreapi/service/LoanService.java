package com.luizgmelo.bookstoreapi.service;

import com.luizgmelo.bookstoreapi.dto.LoanDto;
import com.luizgmelo.bookstoreapi.exceptions.BookNotFoundException;
import com.luizgmelo.bookstoreapi.exceptions.LoanNotFoundException;
import com.luizgmelo.bookstoreapi.exceptions.UserNotFoundException;
import com.luizgmelo.bookstoreapi.model.Book;
import com.luizgmelo.bookstoreapi.model.Loan;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.repository.BookRepository;
import com.luizgmelo.bookstoreapi.repository.LoanRepository;
import com.luizgmelo.bookstoreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LoanService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
    }

    public Page<Loan> getAllLoan(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }

    public Loan createLoan(LoanDto dto) {
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(BookNotFoundException::new);
        User user = userRepository.findUserByUsername(dto.username()).orElseThrow(UserNotFoundException::new);

        Loan newLoan = new Loan();
        newLoan.setBook(book);
        newLoan.setUser(user);
        newLoan.setStartDate(LocalDate.now());
        newLoan.setEndDate(LocalDate.now().plusDays(15));
        if (dto.fine() == null) {
            newLoan.setFine(0.0);
        } else {
            newLoan.setFine(dto.fine());
        }
        if (dto.returned() == null) {
            newLoan.setReturned(false);
        } else {
            newLoan.setReturned(dto.returned());
        }

        return loanRepository.save(newLoan);
    }

    public Page<Loan> listPastDueLoan(LocalDate dateSearch, Pageable pageable) {
        return loanRepository.findByEndDateBeforeAndReturnedIsFalse(dateSearch, pageable);
    }

    public Loan removeLoan(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
        this.calculateFine(loan);
        loanRepository.deleteById(id);
        return loan;
    }

    public void calculateFine(Loan loan) {
        if (LocalDate.now().isAfter(loan.getEndDate())) {
            double dailyRate = 1.0;
            long daysOverdue = ChronoUnit.DAYS.between(loan.getEndDate(), LocalDate.now());
            loan.setFine(daysOverdue * dailyRate);
            loanRepository.save(loan);
        }
    }
}
