package com.wonjung.budget.service;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.dto.response.ExpensesResDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.type.OrderStatus;

import java.time.LocalDate;

public interface ExpenseService {

    Long create(Member member, ExpenseCreateDto createDto);

    ExpenseDetailDto getDetail(Member member, Long expenseId);

    ExpenseDetailDto edit(Member member, Long expenseId, ExpenseCreateDto editDto);

    void delete(Member member, Long expenseId);

    ExpensesResDto getExpenses(Member member, LocalDate startDate, LocalDate endDate, Integer minAmount, Integer maxAmount, OrderStatus orderStatus, String search, String[] categories);
}
