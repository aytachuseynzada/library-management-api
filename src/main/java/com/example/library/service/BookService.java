package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.entity.Book;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<BookResponseDto> getAllBooks(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookRepository.findAllByDeletedFalse(pageable)
                .map(BookMapper::mapToDto);
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return BookMapper.mapToDto(book);
    }

    public BookResponseDto createBook(BookRequestDto dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
        Book saved = bookRepository.save(BookMapper.mapToEntity(dto, author));
        return BookMapper.mapToDto(saved);
    }

    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
        BookMapper.updateEntity(book, dto, author);
        Book updated = bookRepository.save(book);
        return BookMapper.mapToDto(updated);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id: " + id));

        book.setDeleted(true);

        bookRepository.save(book);
    }
}
