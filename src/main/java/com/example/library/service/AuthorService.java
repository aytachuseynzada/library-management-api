package com.example.library.service;

import com.example.library.dao.entity.Author;
import com.example.library.dao.repository.AuthorRepository;
import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Page<AuthorResponseDto> getAllAuthors(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return authorRepository.findAllByDeletedFalse(pageable)
                .map(AuthorMapper::mapToDto);
    }


    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
        return AuthorMapper.mapToDto(author);
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author saved = authorRepository.save(AuthorMapper.mapToEntity(dto));
        return AuthorMapper.mapToDto(saved);
    }

    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
        AuthorMapper.updateEntity(author, dto);
        Author updated = authorRepository.save(author);
        return AuthorMapper.mapToDto(updated);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new AuthorNotFoundException("Author not found with id: " + id));

        author.setDeleted(true);

        authorRepository.save(author);
    }
}
