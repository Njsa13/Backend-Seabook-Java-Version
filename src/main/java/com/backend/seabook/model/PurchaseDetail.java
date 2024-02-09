package com.backend.seabook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_details")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "purchase_detail_id")
    private UUID id;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Column(name = "sub_total_price", nullable = false)
    private Double subTotalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    private Book book;
}
