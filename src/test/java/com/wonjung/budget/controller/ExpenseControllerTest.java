package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.entity.Expense;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.ExpenseRepository;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenseControllerTest extends ControllerTestWithAuth {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("지출 추가 - 성공")
    public void create_expense() throws Exception {
        // given
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(12000)
                .memo("점심으로 마라탕")
                .isExcludedSum(false)
                .category("식비")
                .build();

        // when & then
        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        long createdId = Long.parseLong(resultStr);

        Optional<Expense> optionalExpense = expenseRepository.findById(createdId);
        Assertions.assertTrue(optionalExpense.isPresent());
    }

    @Test
    @DisplayName("지출 추가 - 음수 금액")
    public void create_expense_negative_amount() throws Exception {
        // given
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(-20000)
                .category("식비")
                .build();

        // when & then
        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.INVALID_REQUEST.name()))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("지출 추가 - 잘못된 카테고리")
    public void create_expense_invalid_category() throws Exception {
        // given
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(20000)
                .category("여가생활")
                .build();

        // when & then
        mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.CATEGORY_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 상세 조회 - 성공")
    public void get_expense_detail() throws Exception {
        // given

        // create expense and get id
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(12000)
                .memo("점심으로 마라탕")
                .isExcludedSum(true)
                .category("식비")
                .build();

        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        long expenseId = Long.parseLong(resultStr);

        //when & then
        mockMvc.perform(get("/api/expenses/{expenseId}", expenseId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expended_at").value("2023-11-09T17:12:00"))
                .andExpect(jsonPath("$.amount").value(12000))
                .andExpect(jsonPath("$.memo").value("점심으로 마라탕"))
                .andExpect(jsonPath("$.category").value("식비"))
                .andExpect(jsonPath("$.is_excluded_sum").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 상세 조회 - 다른 사용자의 지출")
    public void get_expense_detail_no_permission() throws Exception {
        // given

        // create Expense with member1 and get id
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(12000)
                .memo("점심으로 마라탕")
                .isExcludedSum(true)
                .category("식비")
                .build();

        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        long expenseId = Long.parseLong(resultStr);

        // create member2 and get auth token
        memberService.signup(new MemberCreateDto(
                memberAccount + "_other",
                memberPassword + "_other",
                memberNickname + "_other",
                true
        ));

        String authToken = memberService.login(new LoginDto(memberAccount + "_other", memberPassword + "_other"));

        //when & then
        mockMvc.perform(get("/api/expenses/{expenseId}", expenseId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.EXPENSE_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 수정 - 성공")
    public void edit_expense() throws Exception {
        // given
        // create Expense and get id
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(12000)
                .memo("점심으로 마라탕")
                .isExcludedSum(true)
                .category("식비")
                .build();

        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        long expenseId = Long.parseLong(resultStr);

        // for edit
        ExpenseCreateDto editDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 18, 0, 0))
                .amount(12500)
                .memo("저녁으로 마라탕")
                .isExcludedSum(false)
                .category("식비")
                .build();

        //when & then
        mockMvc.perform(put("/api/expenses/{expenseId}", expenseId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expended_at").value("2023-11-09T18:00:00"))
                .andExpect(jsonPath("$.amount").value(12500))
                .andExpect(jsonPath("$.memo").value("저녁으로 마라탕"))
                .andExpect(jsonPath("$.category").value("식비"))
                .andExpect(jsonPath("$.is_excluded_sum").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 수정 - 존재하지 않는 지출 아이디")
    public void edit_expense_invalid_expense_id() throws Exception {
        // given
        ExpenseCreateDto editDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 18, 0, 0))
                .amount(12500)
                .memo("저녁으로 마라탕")
                .isExcludedSum(false)
                .category("식비")
                .build();

        //when & then
        mockMvc.perform(put("/api/expenses/{expenseId}", 12345)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.EXPENSE_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("지출 삭제 - 성공")
    public void delete_expense() throws Exception {
        // given

        // create expense and get id
        ExpenseCreateDto createDto = ExpenseCreateDto.builder()
                .expendedAt(LocalDateTime.of(2023, 11, 9, 17, 12, 0))
                .amount(12000)
                .memo("점심으로 마라탕")
                .isExcludedSum(true)
                .category("식비")
                .build();

        MvcResult result = mockMvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        long expenseId = Long.parseLong(resultStr);

        //when & then
        mockMvc.perform(delete("/api/expenses/{expenseId}", expenseId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }


}