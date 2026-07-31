package com.example.library.controller;

import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(
        name = "Book",
        description = "Book management APIs"
)
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @Operation(
            summary = "Get all books",
            description = "Returns paginated and sorted list of books"
    )
    @GetMapping
    public Page<BookResponseDto> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookService.getAllBooks(page, size, sortBy, direction);
    }
    @Operation(
            summary = "Get book by id",
            description = "Returns a single book by its identifier"
    )
    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
    @Operation(
            summary = "Create new book",
            description = "Creates a new book and returns created data"
    )
    @PostMapping
    @ResponseStatus(CREATED)
    public BookResponseDto createBook(@Valid @RequestBody BookRequestDto dto) {
        return bookService.createBook(dto);
    }
    @Operation(
            summary = "Update book",
            description = "Updates existing book information"
    )
    @PutMapping("/{id}")
    public BookResponseDto updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto dto) {
        return bookService.updateBook(id, dto);
    }
    @Operation(
            summary = "Delete book",
            description = "Performs soft delete for a book"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
