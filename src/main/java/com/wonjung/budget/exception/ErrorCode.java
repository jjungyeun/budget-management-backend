package com.wonjung.budget.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // -------- 4xx Error --------
    NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),

    // INVALID REQUEST
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // MEMBER
    DUPLICATE_ACCOUNT(HttpStatus.BAD_REQUEST, "중복된 계정입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "잘못된 아이디 또는 비밀번호입니다."),

    // AUTH
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    EMPTY_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "인증헤더가 비어있습니다."),

    // CATEGORY
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),

    // EXPENSE
    EXPENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지출 내역입니다."),

    // BUDGET
    NEED_ALL_CATEGORIES(HttpStatus.BAD_REQUEST, "모든 카테고리를 포함해야 합니다."),


    // -------- 5xx Error --------
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}