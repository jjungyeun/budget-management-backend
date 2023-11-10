package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.dto.response.ExpensesResDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Expense;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.service.ExpenseService;
import com.wonjung.budget.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public Long create(Member member, ExpenseCreateDto createDto) {
        Category category = getCategoryByName(createDto.category());
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
        Expense expense = getExpenseByMemberAndId(member, expenseId);
        return getExpenseDetailDto(expense);
    }

    @Override
    @Transactional
    public ExpenseDetailDto edit(Member member, Long expenseId, ExpenseCreateDto editDto) {
        Expense expense = getExpenseByMemberAndId(member, expenseId);
        Category category = getCategoryByName(editDto.category());
        expense.edit(
                category,
                editDto.expendedAt(),
                editDto.amount(),
                editDto.isExcludedSum(),
                editDto.memo()
        );
        return getExpenseDetailDto(expense);
    }

    @Override
    @Transactional
    public void delete(Member member, Long expenseId) {
        Expense expense = getExpenseByMemberAndId(member, expenseId);
        expenseRepository.delete(expense);
    }

    @Override
    public ExpensesResDto getExpenses(Member member, LocalDate startDate, LocalDate endDate, Integer minAmount, Integer maxAmount,
                                      OrderStatus orderStatus, String search, String[] categories) {
        // 카테고리 필터가 없으면 모든 카테고리에 대해 조회
        List<String> availableCategories = new ArrayList<>();
        Map<String, Integer> categoryAmounts = new HashMap<>();
        if (categories.length == 0) {
            categoryRepository.findAll().forEach(it -> {
                categoryAmounts.put(it.getName(), 0);
            });
        } else {
            for (String s : categories) {
                if (categoryRepository.findByName(s).isPresent()) {
                    availableCategories.add(s);
                    categoryAmounts.put(s, 0);
                }
            }
        }

        // 조건에 맞는 모든 지출 내역을 불러온다
        List<Expense> expenses = expenseRepository.findAllByMember(
                member, startDate, endDate, minAmount, maxAmount, orderStatus, search, availableCategories);

        // 지출들의 전체 총액 및 카테고리별 총액을 계산하고 DTO로 변환한다
        int totalAmount = 0;
        List<ExpensesResDto.ExpenseResDto> expenseDtos = new ArrayList<>();
        for (Expense expense : expenses) {
            expenseDtos.add(getExpenseResDto(expense));

            String category = expense.getCategory().getName();
            int categorySum = categoryAmounts.get(category) + expense.getAmount();
            categoryAmounts.put(category, categorySum);
            totalAmount += expense.getAmount();
        }

        // 총액이 0원인 (지출이 없는) 카테고리는 결과에서 제외
        List<ExpensesResDto.CategoryAmountDto> categoryAmountDtos = categoryAmounts.entrySet().stream()
                .filter(it -> it.getValue() > 0)
                .map(it -> new ExpensesResDto.CategoryAmountDto(it.getKey(), it.getValue()))
                .toList();

        return new ExpensesResDto(totalAmount, categoryAmountDtos, expenseDtos);
    }

    private Expense getExpenseByMemberAndId(Member member, Long expenseId) {
        return expenseRepository.findByIdAndMember(expenseId, member)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPENSE_NOT_FOUND));
    }

    private Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private ExpenseDetailDto getExpenseDetailDto(Expense expense) {
        String categoryName = expense.getCategory().getName();
        return ExpenseDetailDto.builder()
                .expenseId(expense.getId())
                .expendedAt(expense.getExpendedAt())
                .amount(expense.getAmount())
                .category(categoryName)
                .memo(expense.getMemo())
                .isExcludedSum(expense.getIsExcludedSum())
                .build();
    }

    private static ExpensesResDto.ExpenseResDto getExpenseResDto(Expense expense) {
        String categoryName = expense.getCategory().getName();

        // 메모는 10글자까지 보여줌
        StringBuilder memo = new StringBuilder();
        if (expense.getMemo().length() > 10) {
            memo.append(expense.getMemo(), 0, 10)
                    .append("...");
        } else {
            memo.append(expense.getMemo());
        }

        return ExpensesResDto.ExpenseResDto.builder()
                .expenseId(expense.getId())
                .expendedAt(expense.getExpendedAt())
                .amount(expense.getAmount())
                .category(categoryName)
                .memo(memo.toString())
                .build();
    }


}
