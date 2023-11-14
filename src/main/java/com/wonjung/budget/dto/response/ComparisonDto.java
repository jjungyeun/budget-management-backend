package com.wonjung.budget.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ComparisonDto(
        Integer lastExpense,
        Integer thisExpense,
        Integer increaseRate,
        List<ComparisonInCategory> categories
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ComparisonInCategory(
            String category,
            Integer lastExpense,
            Integer thisExpense,
            Integer increaseRate
    ) {
    }
}
