package com.wonjung.budget.repository;

import com.wonjung.budget.entity.Member;

import java.util.Map;

public interface BudgetRepositoryCustom {
    Map<Long, Long> getCountGroupByCategoryNotMember(Member member);
}
