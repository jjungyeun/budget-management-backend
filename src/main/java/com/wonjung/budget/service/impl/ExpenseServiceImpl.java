package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Expense;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.service.ExpenseService;
import com.wonjung.budget.type.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public Long create(Member member, ExpenseCreateDto createDto) {

        Category category = categoryRepository.findByName(createDto.category())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Expense expense = expenseRepository.save(
                Expense.builder()
                        .member(member)
                        .category(category)
                        .expendedAt(createDto.expendedAt())
                        .amount(createDto.amount())
                        .memo(createDto.memo())
                        .isExcludedSum(createDto.isExcludedSum())
                        .build()
        );

        return expense.getId();
    }

    @Override
    public ExpenseDetailDto getDetail(Member member, Long expenseId) {
        Expense expense = expenseRepository.findByIdAndMember(expenseId, member)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPENSE_NOT_FOUND));

        String categoryName = expense.getCategory().getName();

        return ExpenseDetailDto.builder()
                .expenseId(expenseId)
                .expendedAt(expense.getExpendedAt())
                .amount(expense.getAmount())
                .category(categoryName)
                .memo(expense.getMemo())
                .isExcludedSum(expense.getIsExcludedSum())
                .build();
    }


}
