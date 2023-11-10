package com.wonjung.budget.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "계정은 필수값입니다.")
        String account,
        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password
) {
}