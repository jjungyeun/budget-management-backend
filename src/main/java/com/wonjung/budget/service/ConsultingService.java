package com.wonjung.budget.service;

import com.wonjung.budget.dto.response.ExpenseRecommendDto;
import com.wonjung.budget.dto.response.FeedbackDto;
import com.wonjung.budget.entity.Member;

public interface ConsultingService {
    FeedbackDto getFeedbackExpenseOfToday(Member member);
    ExpenseRecommendDto getRecommendExpenseOfToday(Member member);
}
