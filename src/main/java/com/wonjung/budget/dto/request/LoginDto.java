package com.wonjung.budget.dto.request;

public record LoginDto(
        String account,
        String password
) {
}