package com.wonjung.budget.dto.response;

import java.util.List;

public record CategoriesResDto(
        List<CategoryResDto> categories
) {
    public record CategoryResDto(
            Long id,
            String name
    ) {
    }
}
