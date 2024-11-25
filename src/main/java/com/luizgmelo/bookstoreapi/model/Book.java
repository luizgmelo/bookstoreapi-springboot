package com.luizgmelo.bookstoreapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer bookId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private String genre;
    private String language;
    private Integer pageCount;
}
