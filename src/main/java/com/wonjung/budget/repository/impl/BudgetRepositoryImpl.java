package com.wonjung.budget.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.repository.BudgetRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

import static com.wonjung.budget.entity.QBudget.budget;
import static com.wonjung.budget.entity.QCategory.category;

@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Long> getCountGroupByCategoryNotMember(Member member) {
        return queryFactory.select(category.id, budget.count())
                .from(budget)
                .leftJoin(budget.category)
                .where(budget.member.ne(member))
                .groupBy(category)
                .fetch()
                .stream().collect(Collectors.toMap(
                        tuple -> tuple.get(category.id),
                        tuple -> tuple.get(budget.count())
                ));
    }
}
