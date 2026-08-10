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
import com.example.library.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final FileStorageService fileStorageService;
    private static final List<String> ALLOWED_SORT_FIELDS =
            List.of("id", "title", "isbn", "price", "publishedYear");
    private static final int MAX_PAGE_SIZE = 100;


    public Page<BookResponseDto> getAllBooks(int page, int size, String sortBy, String direction) {

        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookRepository.findAllByDeletedFalse(pageable)
                .map(BookMapper::mapToDto);
    }
    @Cacheable(value = "books", key = "#id")
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return BookMapper.mapToDto(book);
    }

    public BookResponseDto createBook(BookRequestDto dto) {
        Author author = authorRepository.findByIdAndDeletedFalse(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
        Book saved = bookRepository.save(BookMapper.mapToEntity(dto, author));
        return BookMapper.mapToDto(saved);
    }

    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        Author author = authorRepository.findByIdAndDeletedFalse(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + dto.getAuthorId()));
        BookMapper.updateEntity(book, dto, author);
        Book updated = bookRepository.save(book);
        return BookMapper.mapToDto(updated);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with id: " + id));

        book.setDeleted(true);

        bookRepository.save(book);
    }
    public List<BookResponseDto> searchBooks(Integer startYear, Integer endYear, String title, String authorName) {

        Specification<Book> spec = Specification.where(BookSpecification.isNotDeleted())
                .and(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasAuthorName(authorName))
                .and(BookSpecification.publishedBetween(startYear, endYear));

        List<Book> books = bookRepository.findAll(spec);

        return books.stream()
                .map(BookMapper::mapToDto)
                .collect(Collectors.toList());
    }
    public String uploadCoverImage(Long bookId, MultipartFile file) {

        Book book = bookRepository.findByIdAndDeletedFalse(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        String filePath = fileStorageService.storeFile(file);

        book.setCoverImagePath(filePath);
        bookRepository.save(book);

        return filePath;
    }

    public byte[] downloadCoverImage(Long bookId) {

        Book book = bookRepository.findByIdAndDeletedFalse(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getCoverImagePath() == null) {
            throw new IllegalStateException("This book has no cover image");
        }

        return fileStorageService.loadFile(book.getCoverImagePath());
    }
}
