package com.wonjung.budget.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BudgetCreateDto(
        @NotEmpty
        List<BudgetDto> budgets
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record BudgetDto(
            Long categoryId,
            Integer amount
    ) {

    }
}
