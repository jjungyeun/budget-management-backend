package com.wonjung.budget.dto.response;

import java.util.List;

public record BudgetsDto(
        List<BudgetDto> budgets
) {
    public record BudgetDto(
          Long categoryId,
          String categoryName,
          Integer amount
    ) {

    }
}
