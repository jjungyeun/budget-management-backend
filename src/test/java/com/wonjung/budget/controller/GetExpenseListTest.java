package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetExpenseListTest extends ControllerTestWithAuth {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ExpenseService expenseService;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createExpenses();
    }

    @Test
    @DisplayName("지출 목록 조회 - 성공")
    public void get_expenses() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");
        params.add("end_date", "2023-11-10");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenses.length()").value(6))
                .andExpect(jsonPath("$.total_amount").value(46800))
                .andExpect(jsonPath("$.expenses[0].amount").value(12000))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 목록 조회 - 금액 오름차순 정렬")
    public void get_expenses_amount_asc() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");
        params.add("end_date", "2023-11-10");
        params.add("order_by", "amount:asc");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenses.length()").value(6))
                .andExpect(jsonPath("$.total_amount").value(46800))
                .andExpect(jsonPath("$.expenses[0].amount").value(2000))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 목록 조회 - 검색")
    public void get_expenses_memo_search() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");
        params.add("end_date", "2023-11-10");
        params.add("search", "마라탕");
        params.add("order_by", "amount:desc");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenses.length()").value(3))
                .andExpect(jsonPath("$.total_amount").value(37300))
                .andExpect(jsonPath("$.expenses[0].amount").value(13000))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 목록 조회 - 카테고리 필터링")
    public void get_expenses_in_category() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");
        params.add("end_date", "2023-11-10");
        params.add("category", "식비,교통");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenses.length()").value(5))
                .andExpect(jsonPath("$.total_amount").value(44800))
                .andExpect(jsonPath("$.expenses[0].amount").value(12000))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 목록 조회 - 최대/최소 금액 필터링")
    public void get_expenses_between_amount() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");
        params.add("end_date", "2023-11-10");
        params.add("min_amount", "2000");
        params.add("max_amount", "10000");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenses.length()").value(3))
                .andExpect(jsonPath("$.total_amount").value(9500))
                .andExpect(jsonPath("$.expenses[0].amount").value(2000))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 목록 조회 - 날짜 조건 누락")
    public void get_expenses_no_date_param() throws Exception {
        // given
        MultiValueMapAdapter<String, String> params = new LinkedMultiValueMap<>();
        params.add("start_date", "2023-10-10");

        //when & then
        mockMvc.perform(get("/api/expenses")
                        .params(params)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.INVALID_REQUEST.name()))
                .andDo(print());
    }

    private void createExpenses() {
        Member member = memberRepository.findById(memberId).get();

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
                        .expendedAt(LocalDateTime.of(2023, 11, 3, 12, 30, 0))
                        .category("생활")
                        .amount(2000)
                        .memo("다이소")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 5, 9, 30, 0))
                        .category("기타")
                        .amount(2000000)
                        .memo("엄마한테 이체")
                        .isExcludedSum(true)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 7, 19, 30, 0))
                        .category("교통")
                        .amount(12300)
                        .memo("시외버스 예매 (청주 마라탕 투어)")
                        .isExcludedSum(false)
                        .build());
        expenseService.create(member,
                ExpenseCreateDto.builder()
                        .expendedAt(LocalDateTime.of(2023, 11, 1, 15, 30, 0))
                        .category("식비")
                        .amount(4500)
                        .memo("편의점에서 까까")
                        .isExcludedSum(false)
                        .build());
    }

}
