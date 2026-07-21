package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.entity.Book;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;
import com.example.library.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper::mapToDto)
                .toList();
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return BookMapper.mapToDto(book);
    }

    public BookResponseDto createBook(BookRequestDto dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + dto.getAuthorId()));
        Book saved = bookRepository.save(BookMapper.mapToEntity(dto, author));
        return BookMapper.mapToDto(saved);
    }


}
