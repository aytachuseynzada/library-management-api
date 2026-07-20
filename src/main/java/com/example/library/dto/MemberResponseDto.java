package com.example.library.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
}
