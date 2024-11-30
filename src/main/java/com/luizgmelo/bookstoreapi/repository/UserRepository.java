package com.luizgmelo.bookstoreapi.repository;

import com.luizgmelo.bookstoreapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
}
