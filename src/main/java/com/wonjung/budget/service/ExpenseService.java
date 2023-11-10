package com.wonjung.budget.service;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.entity.Member;

public interface ExpenseService {

    Long create(Member member, ExpenseCreateDto createDto);

    ExpenseDetailDto getDetail(Member member, Long expenseId);
}
