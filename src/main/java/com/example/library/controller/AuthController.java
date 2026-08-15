package com.example.library.controller;

import com.example.library.dto.AuthResponseDto;
import com.example.library.dto.LoginRequestDto;
import com.example.library.dto.RegisterRequestDto;
import com.example.library.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Auth",
        description = "Authentication APIs"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account with USER role by default"
    )
    @PostMapping("/register")
    public void register(
            @Valid @RequestBody RegisterRequestDto dto
    ){
        authService.register(dto);
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns a JWT token"
    )
    @PostMapping("/login")
    public AuthResponseDto login(
            @Valid @RequestBody LoginRequestDto dto
    ){
        return authService.login(dto);
    }
}