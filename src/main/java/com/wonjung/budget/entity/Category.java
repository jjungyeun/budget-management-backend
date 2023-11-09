package com.wonjung.budget.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@ToString(of = {"id", "name", "averageRate"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column
    Double averageRate;

    @Builder
    public Category(String name, Double averageRate) {
        Assert.hasText(name, "Category name must not be empty.");

        this.name = name;
        this.averageRate = averageRate;
    }
}
