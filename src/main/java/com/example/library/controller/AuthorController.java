package com.example.library.controller;

import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import com.example.library.service.AuthorService;
import com.example.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    @GetMapping
    public List<AuthorResponseDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public AuthorResponseDto createAuthor(@Valid @RequestBody AuthorRequestDto dto) {
        return authorService.createAuthor(dto);
    }
}
