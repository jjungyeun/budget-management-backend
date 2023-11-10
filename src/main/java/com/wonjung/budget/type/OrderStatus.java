package com.wonjung.budget.type;

import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;

public record OrderStatus(
        OrderType type,
        OrderDirection direction
) {
    public static OrderStatus parse(String orderBy) {
        String[] splits = orderBy.split(":");
        if (splits.length != 2) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        OrderType orderType = OrderType.parse(splits[0]);
        OrderDirection orderDirection = OrderDirection.parse(splits[1]);

        return new OrderStatus(orderType, orderDirection);
    }
}
