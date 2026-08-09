package com.example.library.mapper;

import com.example.library.dao.entity.Author;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import com.example.library.dto.BookSummaryDto;

import java.util.List;
import java.util.stream.Collectors;

public interface AuthorMapper {
    static Author mapToEntity(AuthorRequestDto dto) {
        return Author.builder()
                .name(dto.getName())
                .bio(dto.getBio())
                .build();
    }

    static AuthorResponseDto mapToDto(Author author) {
        List<BookSummaryDto> books = author.getBooks() == null
                ? List.of()
                : author.getBooks().stream()
                .map(book -> BookSummaryDto.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .build())
                .collect(Collectors.toList());

        return AuthorResponseDto.builder()
                .id(author.getId())
                .name(author.getName())
                .bio(author.getBio())
                .books(books)
                .build();
    }

    static void updateEntity(Author author, AuthorRequestDto dto) {
        author.setName(dto.getName());
        author.setBio(dto.getBio());
    }
}
