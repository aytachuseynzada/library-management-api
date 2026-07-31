package com.example.library.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String bio;
    private List<BookSummaryDto> books;
}
