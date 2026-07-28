package com.example.library.controller;

import com.example.library.dto.AuthorRequestDto;
import com.example.library.dto.AuthorResponseDto;
import com.example.library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(
        name = "Author",
        description = "Author management APIs"
)
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    @Operation(
            summary = "Get all authors",
            description = "Returns paginated and sorted list of authors"
    )
    @GetMapping
    public Page<AuthorResponseDto> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return authorService.getAllAuthors(page, size, sortBy, direction);
    }
    @Operation(
            summary = "Get author by id",
            description = "Returns a single author by its identifier"
    )
    @GetMapping("/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }
    @Operation(
            summary = "Create new author",
            description = "Creates a new author and returns created data"
    )
    @PostMapping
    @ResponseStatus(CREATED)
    public AuthorResponseDto createAuthor(@Valid @RequestBody AuthorRequestDto dto) {
        return authorService.createAuthor(dto);
    }
    @Operation(
            summary = "Update author",
            description = "Updates existing author information"
    )
    @PutMapping("/{id}")
    public AuthorResponseDto updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto dto) {
        return authorService.updateAuthor(id, dto);
    }
    @Operation(
            summary = "Delete author",
            description = "Performs soft delete for an author"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}
