package com.wonjung.budget.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExpenseDetailDto(
        Long expenseId,
        LocalDateTime expendedAt,
        Integer amount,
        String category,
        String memo,
        Boolean isExcludedSum
) {
}
