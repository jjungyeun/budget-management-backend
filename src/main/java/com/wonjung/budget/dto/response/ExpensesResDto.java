package com.wonjung.budget.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExpensesResDto(
        Integer totalAmount,
        List<CategoryAmountDto> categoryAmounts,
        List<ExpenseResDto> expenses
) {
    public record CategoryAmountDto(
            String category,
            Integer amount
    ) {
    }

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ExpenseResDto(
            Long expenseId,
            LocalDateTime expendedAt,
            Integer amount,
            String category,
            String memo
    ) {
    }
}
