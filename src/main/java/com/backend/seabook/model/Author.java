package com.backend.seabook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "author_id")
    private UUID id;

    @Column(name = "author_slug", nullable = false, unique = true)
    private String authorSlug;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "amount_book", nullable = false)
    private Integer amountBook;

    @Column(name = "about_author", nullable = false, columnDefinition = "text")
    private String aboutAuthor;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
}
