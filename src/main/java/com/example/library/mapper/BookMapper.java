package com.example.library.mapper;

import com.example.library.dao.entity.Author;
import com.example.library.dao.entity.Book;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;

public interface BookMapper {
    static Book mapToEntity(BookRequestDto dto, Author author) {
        return Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .price(dto.getPrice())
                .publishedYear(dto.getPublishedYear())
                .author(author)
                .build();
    }
    static BookResponseDto mapToDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishedYear(book.getPublishedYear())
                .authorId(book.getAuthor().getId())
                .authorName(book.getAuthor().getName())
                .build();
    }
    static void updateEntity(Book book, BookRequestDto dto, Author author) {
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        book.setPublishedYear(dto.getPublishedYear());
        book.setAuthor(author);
    }
}
