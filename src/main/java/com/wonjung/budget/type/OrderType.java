package com.wonjung.budget.type;

import java.util.Objects;

public enum OrderType {
    DATE,
    AMOUNT
    ;

    public static OrderType parse(String value) {
        String upperValue = value.toUpperCase();
        for (OrderType orderType : OrderType.values()) {
            if (Objects.equals(orderType.name(), upperValue)) {
                return orderType;
            }
        }
        return DATE;
    }
}
