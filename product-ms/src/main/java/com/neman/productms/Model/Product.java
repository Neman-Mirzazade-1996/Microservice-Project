package com.neman.productms.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(nullable = false)
    String brand;
    @Column(unique = true, nullable = false)
    String sku;
    @Column(nullable = false)
    Double price;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    Integer stockQuantity;
    @Column(nullable = false)
    String status;
    @Column(nullable = false)
    String imageUrl;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    String createdAt;
    @UpdateTimestamp
    @Column(nullable = false, insertable = false)
    String updatedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
