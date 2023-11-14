package com.wonjung.budget.service;

import com.wonjung.budget.dto.response.ComparisonDto;
import com.wonjung.budget.entity.Member;

public interface StatisticsService {
    ComparisonDto getComparisonToLastMonth(Member member);

    ComparisonDto getComparisonToLastWeek(Member member);
}
