package com.wonjung.budget.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberEditDto(
        @NotBlank(message = "닉네임은 필수값입니다.")
        String nickname,
        Boolean pushOption
) {
}
