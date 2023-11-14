package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.response.ComparisonDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.service.StatisticsService;
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
public class StatisticsServiceImpl implements StatisticsService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ComparisonDto getComparisonToLastMonth(Member member) {
        // 이번달 오늘까지 사용한 총액
        LocalDate today = LocalDate.now();
        Map<Category, Integer> thisExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(
                        member,
                        getFirstDateOfMonth(today),
                        today
                );

        // 지난달 오늘까지 사용한 총액
        LocalDate lastToday = today.minusMonths(1);
        Map<Category, Integer> lastExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(
                        member,
                        getFirstDateOfMonth(lastToday),
                        lastToday
                );

        // 카테고리별로 지난달과 이번달 지출을 구하고 비교
        return getComparisonDto(thisExpenseSumByCategory, lastExpenseSumByCategory);
    }

    @Override
    public ComparisonDto getComparisonToLastWeek(Member member) {
        // 오늘 사용한 총액
        LocalDate today = LocalDate.now();
        Map<Category, Integer> thisExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(
                        member,
                        today,
                        today
                );

        // 지난주 오늘 사용한 총액
        LocalDate lastToday = today.minusWeeks(1);
        Map<Category, Integer> lastExpenseSumByCategory =
                expenseRepository.getExpenseSumByMemberBetweenDateGroupByCategory(
                        member,
                        lastToday,
                        lastToday
                );

        // 카테고리별로 지난주과 이번주 지출을 구하고 비교
        return getComparisonDto(thisExpenseSumByCategory, lastExpenseSumByCategory);
    }

    private LocalDate getFirstDateOfMonth(LocalDate today) {
        return today.withDayOfMonth(1);
    }

    private ComparisonDto getComparisonDto(
            Map<Category, Integer> thisExpenseSumByCategory, Map<Category, Integer> lastExpenseSumByCategory) {
        int lastTotalSum = 0, thisTotalSum = 0;
        List<ComparisonDto.ComparisonInCategory> comparisons = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            int thisSum = thisExpenseSumByCategory.getOrDefault(category, 0);
            int lastSum = lastExpenseSumByCategory.getOrDefault(category, 0);
            int increaseRate = getIncreaseRate(lastSum, thisSum);
            comparisons.add(
                    new ComparisonDto.ComparisonInCategory(category.getName(), lastSum, thisSum, increaseRate)
            );
            lastTotalSum += lastSum;
            thisTotalSum += thisSum;
        }

        int totalIncreaseRate = getIncreaseRate(lastTotalSum, thisTotalSum);
        return new ComparisonDto(lastTotalSum, thisTotalSum, totalIncreaseRate, comparisons);
    }

    private int getIncreaseRate(int lastSum, int thisSum) {
        if (lastSum == 0 || thisSum == 0) return 0;
        return (int) (((double) thisSum / lastSum) * 100);
    }

}
