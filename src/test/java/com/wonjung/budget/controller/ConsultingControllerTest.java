package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.BudgetCreateDto;
import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.BudgetRepository;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.BudgetService;
import com.wonjung.budget.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConsultingControllerTest extends ControllerTestWithAuth {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private ExpenseService expenseService;

    private Map<Long, Category> categories;
    private Member member;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        categories = categoryRepository.findAll()
                .stream().collect(
                        Collectors.toMap(
                                Category::getId,
                                it -> it
                        )
                );
        member = memberRepository.findById(memberId).get();
    }

    @Test
    @DisplayName("오늘의 지출 안내 - 성공")
    public void get_feedback_of_today() throws Exception {
        // given
        createBudgets();
        createExpenses();

        //when & then
        mockMvc.perform(get("/api/expenses/todays-feedback")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budget").isNumber())
                .andExpect(jsonPath("$.expense").isNumber())
                .andExpect(jsonPath("$.risk").isNumber())
                .andExpect(jsonPath("$.categories").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("오늘의 지출 안내 - 예산이 설정되지 않음")
    public void get_feedback_of_today_no_budget() throws Exception {
        // given
        createExpenses();

        //when & then
        mockMvc.perform(get("/api/expenses/todays-feedback")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.NO_BUDGET.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("오늘의 지출 추천 - 성공")
    public void get_recommend_of_today() throws Exception {
        // given
        createBudgets();
        createExpenses();

        //when & then
        mockMvc.perform(get("/api/expenses/todays-recommend")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budget").isNumber())
                .andExpect(jsonPath("$.ment").isString())
                .andDo(print());
    }

    private void createBudgets() {
        List<BudgetCreateDto.BudgetDto> budgetDtos = categories.keySet().stream().map(categoryId ->
                new BudgetCreateDto.BudgetDto(categoryId, (int) (10000 * categoryId))
        ).toList();
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);
        budgetService.createOrUpdate(member, createDto);
    }

    private void createExpenses() {
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                        .category("식비")
                        .amount(12000)
                        .memo("점심으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 1, 17, 12, 0))
                        .category("식비")
                        .amount(13000)
                        .memo("저녁으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 10, 31, 10, 32, 0))
                        .category("교통")
                        .amount(3000)
                        .memo("강남 약속")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.now())
                        .category("생활")
                        .amount(2000)
                        .memo("다이소")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.now())
                        .category("기타")
                        .amount(2000000)
                        .memo("엄마한테 이체")
                        .isExcludedSum(true)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.now())
                        .category("교통")
                        .amount(12300)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.now())
                        .category("식비")
                        .amount(4500)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
    }
}