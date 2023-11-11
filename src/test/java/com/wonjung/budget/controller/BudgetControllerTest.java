package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.BudgetCreateDto;
import com.wonjung.budget.entity.Budget;
import com.wonjung.budget.entity.Category;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.BudgetRepository;
import com.wonjung.budget.repository.CategoryRepository;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.service.BudgetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BudgetControllerTest extends ControllerTestWithAuth {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetService budgetService;

    private Map<Long, Category> categories;

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
    }

    @Test
    @DisplayName("예산 설정 - 성공")
    public void create_budget() throws Exception {
        // given
        List<BudgetCreateDto.BudgetDto> budgetDtos = categories.keySet().stream().map(categoryId ->
                new BudgetCreateDto.BudgetDto(categoryId, (int) (10000 * categoryId))
        ).toList();
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);

        // when & then
        mockMvc.perform(post("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgets.length()").value(categories.size()))
                .andDo(print());

        List<Category> newCategories = categoryRepository.findAll();
        for (Category newCategory : newCategories) {
            Assertions.assertNotNull(newCategory.getAverageRate());
        }
    }

    @Test
    @DisplayName("예산 설정 - 업데이트 성공")
    public void update_budget() throws Exception {
        // given
        // set budgets
        List<BudgetCreateDto.BudgetDto> budgetDtos = new java.util.ArrayList<>(
                categories.keySet().stream().map(categoryId ->
                new BudgetCreateDto.BudgetDto(categoryId, (int) (10000 * categoryId))
        ).toList());
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);

        mockMvc.perform(post("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgets.length()").value(categories.size()))
                .andDo(print());

        Category categoryToUpdate = categories.get(budgetDtos.get(0).categoryId());
        Member member = memberRepository.findById(memberId).get();
        int amountBefore = budgetRepository.findByMemberAndCategory(member, categoryToUpdate)
                .get()
                .getAmount();

        // for update
        budgetDtos.remove(0);
        budgetDtos.add(new BudgetCreateDto.BudgetDto(categoryToUpdate.getId(), amountBefore + 50000));

        // when & then
        mockMvc.perform(post("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgets.length()").value(categories.size()))
                .andDo(print());

        Budget budgetAfter = budgetRepository.findByMemberAndCategory(member, categoryToUpdate).get();
        Assertions.assertEquals(amountBefore + 50000, budgetAfter.getAmount());
    }

    @Test
    @DisplayName("예산 설정 - 잘못된 카테고리")
    public void create_budget_invalid_category() throws Exception {
        // given
        List<BudgetCreateDto.BudgetDto> budgetDtos = new java.util.ArrayList<>(
                categories.keySet().stream().map(categoryId ->
                        new BudgetCreateDto.BudgetDto(categoryId, (int) (100000 * categoryId))
                ).toList());
        budgetDtos.add(new BudgetCreateDto.BudgetDto(12345L, 100000));
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);

        // when & then
        mockMvc.perform(post("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.CATEGORY_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("예산 설정 - 카테고리 누락")
    public void create_budget_not_all_category() throws Exception {
        // given
        List<BudgetCreateDto.BudgetDto> budgetDtos = new java.util.ArrayList<>(
                categories.keySet().stream().map(categoryId ->
                new BudgetCreateDto.BudgetDto(categoryId, (int) (100000 * categoryId))
        ).toList());
        budgetDtos.remove(0);
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);

        // when & then
        mockMvc.perform(post("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.NEED_ALL_CATEGORIES.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("예산 조회 - 성공")
    public void get_budgets() throws Exception {
        // given
        Member member = memberRepository.findById(memberId).get();

        List<BudgetCreateDto.BudgetDto> budgetDtos = categories.keySet().stream().map(categoryId ->
                new BudgetCreateDto.BudgetDto(categoryId, (int) (100000 * categoryId))
        ).toList();
        BudgetCreateDto createDto = new BudgetCreateDto(budgetDtos);
        budgetService.createOrUpdate(member, createDto);

        // when & then
        mockMvc.perform(get("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgets.length()").value(categories.size()))
                .andExpect(jsonPath("$.budgets[0].amount").isNumber())
                .andDo(print());
    }

    @Test
    @DisplayName("예산 조회 - 예산 설정 안된 경우")
    public void get_budgets_no_budgets() throws Exception {
        // when & then
        mockMvc.perform(get("/api/budgets")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgets.length()").value(categories.size()))
                .andExpect(jsonPath("$.budgets[0].amount").value(0))
                .andDo(print());
    }

}