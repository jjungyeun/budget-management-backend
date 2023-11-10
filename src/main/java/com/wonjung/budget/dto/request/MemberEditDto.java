package com.wonjung.budget.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberEditDto(
        @NotBlank(message = "닉네임은 필수값입니다.")
        String nickname,
        Boolean pushOption
) {
}
