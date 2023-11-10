package com.wonjung.budget.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberCreateDto(
        String account,
        @Pattern(regexp = "^(?=(.*\\d.*))(?=(.*[a-zA-Z].*))(?=(.*[!@#$%^&*()].*)).{10,}$",
                message = "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상 포함하고, 최소 10자 이상이어야 합니다.")
        String password,
        String nickname,
        Boolean pushOption
) {
}
