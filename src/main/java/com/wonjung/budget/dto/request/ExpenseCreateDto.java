package com.wonjung.budget.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExpenseCreateDto(
        @NotNull
        LocalDateTime expendedAt,
        @NotNull
        @Min(value = 0, message = "지출 금액은 0원 이상이어야 합니다.")
        Integer amount,
        @NotBlank
        String category,
        String memo,
        Boolean isExcludedSum
) {
}
