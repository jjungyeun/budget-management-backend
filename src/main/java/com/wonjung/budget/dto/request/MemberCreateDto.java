package com.wonjung.budget.dto.request;

public record MemberCreateDto(
        String account,
        String password,
        String nickname,
        Boolean pushOption
) {
}
