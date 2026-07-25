package com.example.library.service;

import com.example.library.dao.entity.Book;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;
import com.example.library.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void shouldReturnBooksWithPagination() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setDeleted(false);


        Page<Book> bookPage =
                new PageImpl<>(List.of(book));


        when(bookRepository.findAllByDeletedFalse(any(Pageable.class)))
                .thenReturn(bookPage);


        Page<BookResponseDto> result =
                bookService.getAllBooks(
                        0,
                        10,
                        "id",
                        "asc"
                );


        assertEquals(1, result.getContent().size());
        assertEquals(
                "Clean Code",
                result.getContent().get(0).getTitle()
        );


        verify(bookRepository)
                .findAllByDeletedFalse(any(Pageable.class));
    }
    @Test
    void shouldReturnBookById() {

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K Rowling");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor(author);

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(book));

        BookResponseDto result = bookService.getBookById(1L);

        assertEquals("Clean Code", result.getTitle());

        verify(bookRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookById(1L)
        );

        verify(bookRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldCreateBook() {

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K Rowling");

        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Clean Code");
        dto.setIsbn("123456");
        dto.setPrice(BigDecimal.valueOf(50));
        dto.setPublishedYear(2008);
        dto.setAuthorId(1L);

        Book savedBook = Book.builder()
                .id(1L)
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .price(dto.getPrice())
                .publishedYear(dto.getPublishedYear())
                .author(author)
                .build();

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(author));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

        BookResponseDto result = bookService.createBook(dto);

        assertEquals("Clean Code", result.getTitle());

        verify(bookRepository).save(any(Book.class));
    }
    @Test
    void shouldUpdateBook() {

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K Rowling");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Old Title");
        book.setAuthor(author);

        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("New Title");
        dto.setIsbn("123456");
        dto.setPrice(BigDecimal.valueOf(50));
        dto.setPublishedYear(2024);
        dto.setAuthorId(1L);

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(book));

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(author));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(book);

        BookResponseDto result = bookService.updateBook(1L, dto);

        assertEquals("New Title", result.getTitle());

        verify(bookRepository).save(book);
    }
    @Test
    void shouldDeleteBook() {

        Book book = new Book();
        book.setId(1L);
        book.setDeleted(false);

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        assertEquals(true, book.isDeleted());

        verify(bookRepository).save(book);
    }
    @Test
    void shouldThrowBookNotFoundExceptionWhenDeletingNonExistingBook() {

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(1L)
        );
    }
}

