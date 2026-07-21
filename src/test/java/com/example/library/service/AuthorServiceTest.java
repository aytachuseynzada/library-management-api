package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dto.AuthorResponseDto;
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
}

