package com.luizgmelo.bookstoreapi.dto;

import com.luizgmelo.bookstoreapi.model.UserRole;

public record RegisterDto(String username, String email, String password, UserRole role) {
}
