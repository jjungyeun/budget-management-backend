package com.wonjung.budget.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wonjung.budget.entity.*;
import com.wonjung.budget.repository.ExpenseRepositoryCustom;
import com.wonjung.budget.type.OrderDirection;
import com.wonjung.budget.type.OrderStatus;
import com.wonjung.budget.type.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wonjung.budget.entity.QCategory.*;
import static com.wonjung.budget.entity.QExpense.*;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Expense> findAllByMember(Member member, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount, OrderStatus orderStatus, String search, List<String> categories) {
        JPAQuery<Expense> query = queryFactory.selectFrom(expense)
                .leftJoin(expense.category, category)
                .where(
                        expense.member.eq(member),
                        expense.isExcludedSum.isFalse(),
                        dateBetween(startDate, endDate),
                        amountBetween(minAmount, maxAmount),
                        memoLike(search),
                        categoryIn(categories)
                );
        if (orderStatus.type() == OrderType.DATE) {
            return query.orderBy(orderByDate(orderStatus))
                    .fetch();
        } else {
            return query.orderBy(orderByAmount(orderStatus))
                    .fetch();
        }
    }

    @Override
    public Map<Category, Integer> getExpenseSumByMemberBetweenDateGroupByCategory(Member member, LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(category, expense.amount.sum())
                .from(expense)
                .leftJoin(expense.category, category)
                .where(
                        expense.member.eq(member),
                        expense.isExcludedSum.isFalse(),
                        dateBetween(startDate, endDate)
                )
                .groupBy(category.id)
                .orderBy(category.id.asc())
                .fetch()
                .stream().collect(Collectors.toMap(
                        tuple -> tuple.get(category),
                        tuple -> tuple.get(expense.amount.sum())
                ));
    }

    private BooleanExpression dateBetween(LocalDate start, LocalDate end) {
        return expense.expendedAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    private BooleanExpression amountBetween(int min, int max) {
        return expense.amount.between(min, max);
    }

    private BooleanExpression memoLike(String search) {
        return StringUtils.hasText(search) ? expense.memo.contains(search) : null;
    }

    private BooleanExpression categoryIn(List<String> categories) {
        return categories.isEmpty() ? null : category.name.in(categories);
    }

    private OrderSpecifier<LocalDateTime> orderByDate(OrderStatus orderStatus) {
        return orderStatus.direction() == OrderDirection.DESC ? expense.expendedAt.desc() : expense.expendedAt.asc();
    }

    private OrderSpecifier<Integer> orderByAmount(OrderStatus orderStatus) {
        return orderStatus.direction() == OrderDirection.DESC ? expense.amount.desc() : expense.amount.asc();
    }

}
