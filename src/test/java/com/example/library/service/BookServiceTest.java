package com.example.library.service;

import com.example.library.dao.entity.Book;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dto.BookResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

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
}

