package com.wonjung.budget.service;

import com.wonjung.budget.dto.request.BudgetCreateDto;
import com.wonjung.budget.dto.response.BudgetsDto;
import com.wonjung.budget.entity.Member;

public interface BudgetService {
    BudgetsDto create(Member member, BudgetCreateDto createDto);
    BudgetsDto getBudgets(Member member);
}
