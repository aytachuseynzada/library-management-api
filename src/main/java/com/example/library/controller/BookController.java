package com.example.library.controller;

import com.example.library.dto.BookRequestDto;
import com.example.library.dto.BookResponseDto;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @Operation(
            summary = "Search books",
            description = "Search books by title, author name, or published year range"
    )
    @GetMapping("/search")
    public List<BookResponseDto> searchBooks(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName) {

        return bookService.searchBooks(startYear, endYear, title, authorName);
    }
    @Operation(
            summary = "Upload book cover image",
            description = "Uploads a cover image (JPG/PNG, max 5MB) for a book"
    )
    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCoverImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        String filePath = bookService.uploadCoverImage(id, file);
        return ResponseEntity.ok("Cover image uploaded successfully: " + filePath);
    }

    @Operation(
            summary = "Download book cover image",
            description = "Downloads the cover image of a book"
    )
    @GetMapping("/{id}/cover")
    public ResponseEntity<byte[]> downloadCoverImage(@PathVariable Long id) {

        byte[] imageData = bookService.downloadCoverImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }
}
