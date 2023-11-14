package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Expense;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.type.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseRepositoryCustom {
    List<Expense> findAllByMember(Member member, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
                                  OrderStatus orderStatus, String search, List<String> categories);
    Map<Category, Integer> getExpenseSumByMemberBetweenDateGroupByCategory(Member member, LocalDate startDate, LocalDate endDate);

}
