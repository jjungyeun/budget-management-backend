package com.wonjung.budget.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"id", "expendedAt", "amount", "isExcludedSum", "memo"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @Column(name = "expense_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(nullable = false)
    LocalDateTime expendedAt;

    @Column(name = "expense_amount", nullable = false)
    Integer amount;

    @Column(nullable = false)
    Boolean isExcludedSum;

    String memo;

    @Builder
    public Expense(Member member, Category category, LocalDateTime expendedAt, Integer amount, Boolean isExcludedSum, String memo) {
        Assert.notNull(member, "Member of expense must not be null.");
        Assert.notNull(category, "Category of expense must not be null.");
        Assert.notNull(amount, "Expense amount must not be null.");
        Assert.isTrue(amount > 0, "Expense amount must greater than 0.");
        Assert.notNull(expendedAt, "Expense datetime must not be null.");

        this.member = member;
        this.category = category;
        this.expendedAt = expendedAt;
        this.amount = amount;
        this.isExcludedSum = isExcludedSum != null ? isExcludedSum : false;
        this.memo = memo;
    }
}
