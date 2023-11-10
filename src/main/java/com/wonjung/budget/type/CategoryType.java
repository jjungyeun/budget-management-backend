package com.wonjung.budget.type;

public enum CategoryType {
    FOOD("식비"),
    TRANSPORTATION("교통"),
    LEISURE("여가"),
    HEALTH("건강"),
    LIFE("생활"),
    EVENT("경조사"),
    ETC("기타");

    private final String ko;

    CategoryType(String ko) {
        this.ko = ko;
    }

    public String getKo() {
        return ko;
    }
}
