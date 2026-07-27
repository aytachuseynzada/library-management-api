package com.example.library.controller;

import com.example.library.dto.AuthResponseDto;
import com.example.library.dto.LoginRequestDto;
import com.example.library.dto.RegisterRequestDto;
import com.example.library.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public void register(
            @Valid @RequestBody RegisterRequestDto dto
    ){
        authService.register(dto);
    }


    @PostMapping("/login")
    public AuthResponseDto login(
            @Valid @RequestBody LoginRequestDto dto
    ){
        return authService.login(dto);
    }
}
