package com.luizgmelo.bookstoreapi.controller;

import com.luizgmelo.bookstoreapi.dto.AuthenticationDto;
import com.luizgmelo.bookstoreapi.dto.LoginRegisterResponseDto;
import com.luizgmelo.bookstoreapi.dto.RegisterDto;
import com.luizgmelo.bookstoreapi.model.User;
import com.luizgmelo.bookstoreapi.repository.UserRepository;
import com.luizgmelo.bookstoreapi.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginRegisterResponseDto> login(@RequestBody @Valid AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        var response = new LoginRegisterResponseDto(user.getUsernameField(), token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginRegisterResponseDto> register(@RequestBody @Valid RegisterDto data) {
        if (repository.findUserByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.username(), data.email(), encryptedPassword, data.role());
        repository.save(newUser);

        var token = tokenService.generateToken(newUser);
        var response = new LoginRegisterResponseDto(newUser.getUsernameField(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
