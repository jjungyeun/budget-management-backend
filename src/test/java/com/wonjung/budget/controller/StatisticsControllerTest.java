package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatisticsControllerTest extends ControllerTestWithAuth {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ExpenseService expenseService;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    @DisplayName("월 대비 통계 조회 - 성공")
    public void get_comparison_in_month() throws Exception {
        // given
        createExpenses();

        //when & then
        mockMvc.perform(get("/api/statistics/month")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_expense").isNumber())
                .andExpect(jsonPath("$.this_expense").isNumber())
                .andExpect(jsonPath("$.increase_rate").isNumber())
                .andExpect(jsonPath("$.categories").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("요일 대비 통계 조회 - 성공")
    public void get_comparison_in_week() throws Exception {
        // given
        createExpenses();

        //when & then
        mockMvc.perform(get("/api/statistics/day-of-week")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_expense").isNumber())
                .andExpect(jsonPath("$.this_expense").isNumber())
                .andExpect(jsonPath("$.increase_rate").isNumber())
                .andExpect(jsonPath("$.categories").isArray())
                .andDo(print());
    }

    private void createExpenses() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime lastMonthDay = today.minusMonths(1);
        LocalDateTime lastWeekDay = today.minusWeeks(1);

        Member member = memberRepository.findById(memberId).get();

        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("식비")
                        .amount(12000)
                        .memo("점심으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("식비")
                        .amount(17000)
                        .memo("저녁으로 마라탕")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastMonthDay)
                        .category("교통")
                        .amount(3300)
                        .memo("강남 약속")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("생활")
                        .amount(5000)
                        .memo("다이소")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("기타")
                        .amount(2000000)
                        .memo("엄마한테 이체")
                        .isExcludedSum(true)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("교통")
                        .amount(12000)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(today)
                        .category("식비")
                        .amount(4500)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("기타")
                        .amount(500)
                        .memo("이체 수수료")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("교통")
                        .amount(3500)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(lastWeekDay)
                        .category("식비")
                        .amount(2300)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
    }


}