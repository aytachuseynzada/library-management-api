package com.example.library.mapper;

import com.example.library.dao.entity.Author;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;

public interface AuthorMapper {
    static Author mapToEntity(AuthorRequestDto dto) {
        return Author.builder()
                .name(dto.getName())
                .bio(dto.getBio())
                .build();
    }
    static AuthorResponseDto mapToDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .name(author.getName())
                .bio(author.getBio())
                .build();
    }
    static void updateEntity(Author author, AuthorRequestDto dto) {
        author.setName(dto.getName());
        author.setBio(dto.getBio());
    }
}
