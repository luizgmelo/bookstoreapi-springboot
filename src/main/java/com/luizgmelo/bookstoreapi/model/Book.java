package com.luizgmelo.bookstoreapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Book extends RepresentationModel<Book> {
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Rating> ratings;
}
