package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.example.library.exception.AuthorNotFoundException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;


    @Test
    void shouldReturnAuthorsWithPagination() {

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K Rowling");
        author.setBio("Fantasy writer");
        author.setDeleted(false);


        Page<Author> authorPage =
                new PageImpl<>(List.of(author));


        when(authorRepository.findAllByDeletedFalse(any(Pageable.class)))
                .thenReturn(authorPage);


        Page<AuthorResponseDto> result =
                authorService.getAllAuthors(
                        0,
                        10,
                        "id",
                        "asc"
                );


        assertEquals(1, result.getContent().size());
        assertEquals(
                "J.K Rowling",
                result.getContent().get(0).getName()
        );


        verify(authorRepository)
                .findAllByDeletedFalse(any(Pageable.class));
    }
    @Test
    void shouldReturnAuthorById() {

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K Rowling");
        author.setBio("Fantasy writer");

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(author));

        AuthorResponseDto result = authorService.getAuthorById(1L);

        assertEquals("J.K Rowling", result.getName());

        verify(authorRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldThrowAuthorNotFoundExceptionWhenAuthorDoesNotExist() {

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.getAuthorById(1L)
        );

        verify(authorRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldCreateAuthor() {

        AuthorRequestDto dto = new AuthorRequestDto();
        dto.setName("J.K Rowling");
        dto.setBio("Fantasy writer");

        Author author = Author.builder()
                .name(dto.getName())
                .bio(dto.getBio())
                .build();

        Author savedAuthor = Author.builder()
                .id(1L)
                .name(dto.getName())
                .bio(dto.getBio())
                .build();

        when(authorRepository.save(any(Author.class)))
                .thenReturn(savedAuthor);

        AuthorResponseDto result = authorService.createAuthor(dto);

        assertEquals("J.K Rowling", result.getName());

        verify(authorRepository).save(any(Author.class));
    }
    @Test
    void shouldUpdateAuthor() {

        Author author = new Author();
        author.setId(1L);
        author.setName("Old Name");
        author.setBio("Old Bio");

        AuthorRequestDto dto = new AuthorRequestDto();
        dto.setName("New Name");
        dto.setBio("New Bio");

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(author));

        when(authorRepository.save(any(Author.class)))
                .thenReturn(author);

        AuthorResponseDto result = authorService.updateAuthor(1L, dto);

        assertEquals("New Name", result.getName());

        verify(authorRepository).save(author);
    }
    @Test
    void shouldDeleteAuthor() {

        Author author = new Author();
        author.setId(1L);
        author.setDeleted(false);

        when(authorRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        assertEquals(true, author.isDeleted());

        verify(authorRepository).save(author);
    }
}

