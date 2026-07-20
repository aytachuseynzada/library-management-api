package com.example.library.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer publishedYear;
    private Long authorId;
    private String authorName;
}
