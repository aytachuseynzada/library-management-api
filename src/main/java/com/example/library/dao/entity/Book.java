package com.example.library.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer publishedYear;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
