package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.*;
import com.luizgmelo.bookstoreapi.model.Loan;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.model.UserRole;
import com.luizgmelo.bookstoreapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<LoanDto>> getAllLoans(Pageable pageable) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read user authenticate details");
        }

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You do not have permission to create Loan");
        }

        Page<Loan> loans = loanService.getAllLoan(pageable);
        List<Loan> loansList = loans.getContent();

        for (Loan loan : loansList) {
            loanService.calculateFine(loan);
        }

        List<LoanDto> response = LoanMapper.toDTOList(loansList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{endDate}")
    public ResponseEntity<List<LoanDto>> listLoansNotReturned(@PathVariable LocalDate endDate, Pageable pageable) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read user authenticate details");
        }

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You do not have permission to create Loan");
        }

        Page<Loan> loans = loanService.listPastDueLoan(endDate, pageable);
        List<Loan> loansList = loans.getContent();

        for (Loan loan : loansList) {
            loanService.calculateFine(loan);
        }

        List<LoanDto> response = LoanMapper.toDTOList(loansList);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<LoanDto> createNewLoan(@RequestBody LoanDto dto) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read user authenticate details");
        }

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You do not have permission to create Loan");
        }

        Loan newLoan = loanService.createLoan(dto);
        LoanDto response = LoanMapper.toDTO(newLoan);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoanDto> deleteLoan(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("Failed to read user authenticate details");
        }

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You do not have permission to create Loan");
        }

        Loan removed = loanService.removeLoan(id);

        LoanDto response = LoanMapper.toDTO(removed);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
