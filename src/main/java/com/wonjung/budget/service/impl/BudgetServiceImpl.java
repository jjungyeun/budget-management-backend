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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BudgetsDto create(Member member, BudgetCreateDto createDto) {
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
            Budget budget = Budget.builder()
                    .member(member)
                    .category(category)
                    .amount(budgetDto.amount())
                    .build();
            budgets.add(budget);

            responses.add(new BudgetsDto.BudgetDto(
                    category.getId(), category.getName(), budget.getAmount()
            ));
        }

        budgetRepository.saveAll(budgets);
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
}
