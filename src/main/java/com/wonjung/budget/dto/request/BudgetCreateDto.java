package com.wonjung.budget.dto.request;

import java.util.List;

public record BudgetCreateDto(
        List<BudgetDto> budgets
) {
    public record BudgetDto(
            Long categoryId,
            Integer amount
    ) {

    }
}
