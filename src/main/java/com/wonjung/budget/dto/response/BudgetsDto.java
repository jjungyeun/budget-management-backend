package com.wonjung.budget.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

public record BudgetsDto(
        List<BudgetDto> budgets
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record BudgetDto(
          Long categoryId,
          String categoryName,
          Integer amount
    ) {

    }
}
