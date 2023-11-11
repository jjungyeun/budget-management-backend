package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.request.BudgetCreateDto;
import com.wonjung.budget.dto.response.BudgetsDto;
import com.wonjung.budget.entity.Budget;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.BudgetRepository;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.service.BudgetService;
import com.wonjung.budget.type.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BudgetsDto createOrUpdate(Member member, BudgetCreateDto createDto) {
        Map<Long, Category> categories = categoryRepository.findAll()
                .stream().collect(
                        Collectors.toMap(
                                Category::getId,
                                it -> it
                        )
                );

        if (createDto.budgets().size() < categories.size()) {
            throw new CustomException(ErrorCode.NEED_ALL_CATEGORIES);
        }

        List<Budget> budgets = new ArrayList<>();
        List<BudgetsDto.BudgetDto> responses = new ArrayList<>();
        for (BudgetCreateDto.BudgetDto budgetDto : createDto.budgets()) {
            if (!categories.containsKey(budgetDto.categoryId())) {
                throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            }

            Category category = categories.get(budgetDto.categoryId());

            // 기존에 설정된 예산이 존재한다면 업데이트, 없었다면 생성
            Optional<Budget> budgetOptional = budgetRepository.findByMemberAndCategory(member, category);
            Budget budget;
            if (budgetOptional.isPresent()) {
                budget = budgetOptional.get();
                budget.editAmount(budgetDto.amount());
                budgets.add(budget);
            } else {
                budget = Budget.builder()
                        .member(member)
                        .category(category)
                        .amount(budgetDto.amount())
                        .build();
                budgets.add(budget);
            }

            responses.add(new BudgetsDto.BudgetDto(
                    category.getId(), category.getName(), budget.getAmount()
            ));
        }

        budgetRepository.saveAll(budgets);

        // 카테고리 평균 업데이트
        updateCategoryAverage(categories, budgets, member);

        return new BudgetsDto(responses);
    }

    @Override
    public BudgetsDto getBudgets(Member member) {
        List<BudgetsDto.BudgetDto> responses = new ArrayList<>();
        List<Budget> budgets = budgetRepository.findAllByMemberWithCategory(member);

        // 설정된 예산이 없는 경우 모든 카테고리의 예산을 0원으로 반환
        if (budgets.isEmpty()) {
            categoryRepository.findAll().forEach(it ->
                    responses.add(
                            new BudgetsDto.BudgetDto(it.getId(), it.getName(), 0)
                    )
            );
        } else {
            for (Budget budget : budgets) {
                Category category = budget.getCategory();
                responses.add(
                        new BudgetsDto.BudgetDto(category.getId(), category.getName(), budget.getAmount())
                );
            }
        }

        return new BudgetsDto(responses);
    }

    private void updateCategoryAverage(Map<Long, Category> categories, List<Budget> memberBudgets, Member member) {
        int restRate = 100;
        long memberBudgetSum = memberBudgets.stream().mapToLong(Budget::getAmount).sum();
        Map<Long, Long> budgetCountsByCategory = budgetRepository.getCountGroupByCategoryNotMember(member);

        for (Budget memberBudget : memberBudgets) {
            // 기타 카테고리는 별도 계산
            if (memberBudget.getCategory().getName().equals(CategoryType.ETC.getKo())) {
                continue;
            }

            long categoryId = memberBudget.getCategory().getId();
            long categoryCount = budgetCountsByCategory.containsKey(categoryId) ?
                    budgetCountsByCategory.get(categoryId) : 0;
            Category category = categories.get(categoryId);

            // 사용자의 카테고리별 예산 비율을 구하여 카테고리 평균 예산 비율 업데이트
            // 대략적인 평균이 필요한 것이므로 간단한 계산을 위해 퍼센트 소숫점을 내림한다.
            int memberRate = (int) (((double) memberBudget.getAmount() / memberBudgetSum) * 100);
            int categoryNewRate = getCategoryNewRate(category, categoryCount, memberRate);
            category.updateAverageRate(categoryNewRate);
            restRate -= categoryNewRate;
        }

        Optional<Category> etcOptional = categories.values().stream()
                .filter(it -> it.getName().equals(CategoryType.ETC.getKo()))
                .findFirst();
        if (etcOptional.isPresent()) {
            Category etcCategory = etcOptional.get();
            etcCategory.updateAverageRate(restRate);
        }
    }

    private int getCategoryNewRate(Category category, long categoryCount, int memberRate) {
        int categoryAverageRate = category.getAverageRate() != null ?
                category.getAverageRate() : 0;
        long categorySum = categoryAverageRate * categoryCount;
        return (int) ((categorySum + memberRate) / (categoryCount + 1));
    }
}
