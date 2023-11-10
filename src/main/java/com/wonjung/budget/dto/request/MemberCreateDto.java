package com.wonjung.budget.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberCreateDto(
        @NotBlank(message = "계정은 필수값입니다.")
        String account,
        @NotBlank(message = "비밀번호는 필수값입니다.")
        @Pattern(regexp = "^(?=(.*\\d.*))(?=(.*[a-zA-Z].*))(?=(.*[!@#$%^&*()].*)).{10,}$",
                message = "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상 포함하고, 최소 10자 이상이어야 합니다.")
        String password,
        @NotBlank(message = "닉네임은 필수값입니다.")
        String nickname,
        Boolean pushOption
) {
}
