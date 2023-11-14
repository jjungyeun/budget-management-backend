package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.response.ExpenseRecommendDto;
import com.wonjung.budget.dto.response.FeedbackDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.BudgetRepository;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.service.ConsultingService;
import com.wonjung.budget.type.ConsultingMent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultingServiceImpl implements ConsultingService {

    private static final int MIN_BUDGET_OF_DAY = 10000;

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    // 오늘 지출한 총액과 카테고리별 금액 반환
    @Override
    public FeedbackDto getFeedbackExpenseOfToday(Member member) {
        // 오늘 추천 예산
        LocalDate today = LocalDate.now();
        int expenseSumUntilYesterday = getExpenseUntilYesterday(member, today);
        int budgetOfToday = getBudgetOfToday(member, today, expenseSumUntilYesterday);

        // 오늘의 지출 합계
        Map<Category, Integer> todayExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(member, today, today);
        int expenseOfToday = 0;
        List<FeedbackDto.CategoryExpenseDto> categoryExpenseDtos = new ArrayList<>();
        for (Map.Entry<Category, Integer> entry : todayExpenseSumByCategory.entrySet()) {
            Category category = entry.getKey();
            int expenseSum = entry.getValue();
            categoryExpenseDtos.add(new FeedbackDto.CategoryExpenseDto(category.getName(), expenseSum));
            expenseOfToday += expenseSum;
        }

        int risk = getRisk(budgetOfToday,  expenseOfToday);
        return new FeedbackDto(budgetOfToday, expenseOfToday, risk, categoryExpenseDtos);
    }

    // 예산을 만족하기 위해 오늘 지출 가능한 금액 제안
    @Override
    public ExpenseRecommendDto getRecommendExpenseOfToday(Member member) {
        LocalDate today = LocalDate.now();
        int expenseUntilYesterday = getExpenseUntilYesterday(member, today);
        int budgetOfToday = getBudgetOfToday(member, today, expenseUntilYesterday);

        // 오늘이 1일이면 첫날 멘트 반환
        if (today.getDayOfMonth() == 1) {
            return new ExpenseRecommendDto(budgetOfToday, ConsultingMent.START.getMent());
        }

        // 어제까지의 지출 위험도 계산
        LocalDate yesterday = getYesterday(today);
        Integer budgetSumOfMonth = budgetRepository.getAmountSumByMember(member);
        int budgetUntilYesterday = (budgetSumOfMonth / yesterday.lengthOfMonth()) * yesterday.getDayOfMonth();
        int risk = getRisk(budgetUntilYesterday, expenseUntilYesterday);

        ConsultingMent ment;
        if (risk >= 120) {
            ment = ConsultingMent.OVER;
        } else if (risk >= 105) {
            ment = ConsultingMent.ALMOST;
        } else if (risk >= 95) {
            ment = ConsultingMent.GOOD;
        } else {
            ment = ConsultingMent.SAVING;
        }

        return new ExpenseRecommendDto(budgetOfToday, ment.getMent());
    }

    private int getBudgetOfToday(Member member, LocalDate today, int expenseBefore) {
        Integer budgetSum = budgetRepository.getAmountSumByMember(member);
        if (budgetSum == null || budgetSum == 0) {
            throw new CustomException(ErrorCode.NO_BUDGET);
        }

        int restBudget = budgetSum - expenseBefore;
        int restDay = getRestDayThisMonth(today);
        if (restBudget > 0) {
            return Integer.max(MIN_BUDGET_OF_DAY,((restBudget / restDay) / 1000) * 1000);
        } else {
            return MIN_BUDGET_OF_DAY;
        }
    }

    // 어제까지의 지출 구하기 (오늘이 1일이면 0원)
    private int getExpenseUntilYesterday(Member member, LocalDate today) {
        if (today.getDayOfMonth() == 1) {
            return 0;
        }
        Map<Category, Integer> beforeExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(
                        member,
                        getFirstDateOfMonth(today),
                        getYesterday(today)
                );
        return beforeExpenseSumByCategory.values().stream().mapToInt(it -> it).sum();
    }

    private int getRisk(int budget, int expense) {
        return (int) (((double) expense / budget) * 100);
    }

    private LocalDate getFirstDateOfMonth(LocalDate today) {
        return today.withDayOfMonth(1);
    }

    private LocalDate getYesterday(LocalDate today) {
        return today.minusDays(1);
    }

    private int getRestDayThisMonth(LocalDate today) {
        return today.lengthOfMonth() - today.getDayOfMonth() + 1;
    }
}
