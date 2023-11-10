package com.wonjung.budget.type;

import java.util.Objects;

public enum OrderDirection {
    ASC,
    DESC
    ;

    public static OrderDirection parse(String value) {
        String upperValue = value.toUpperCase();
        for (OrderDirection direction : OrderDirection.values()) {
            if (Objects.equals(direction.name(), upperValue)) {
                return direction;
            }
        }
        return DESC;
    }
}
