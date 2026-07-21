package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import com.example.library.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::mapToDto)
                .toList();
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return AuthorMapper.mapToDto(author);
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author saved = authorRepository.save(AuthorMapper.mapToEntity(dto));
        return AuthorMapper.mapToDto(saved);
    }
}
