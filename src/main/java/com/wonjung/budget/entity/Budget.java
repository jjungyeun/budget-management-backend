package com.wonjung.budget.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@ToString(of = {"id", "amount"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {

    @Id
    @Column(name = "budget_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "budget_amount", nullable = false)
    Integer amount;

    @Builder
    public Budget(Member member, Category category, Integer amount) {
        Assert.notNull(member, "Member of budget must not be null.");
        Assert.notNull(category, "Category of budget must not be null.");
        Assert.notNull(amount, "Budget amount must not be null.");
        Assert.isTrue(amount >= 0, "Budget amount must equal or greater than 0.");

        this.member = member;
        this.category = category;
        this.amount = amount;
    }
}
