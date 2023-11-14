package com.wonjung.budget.dto.response;

import java.util.List;

public record FeedbackDto(
        Integer budget,
        Integer expense,
        Integer risk,
        List<CategoryExpenseDto> categories
) {
    public record CategoryExpenseDto(
            String category,
            Integer expense
    ) {

    }
}
