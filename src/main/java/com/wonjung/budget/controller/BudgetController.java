package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.BudgetCreateDto;
import com.wonjung.budget.dto.response.BudgetsDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetsDto> createOrUpdateBudget(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody BudgetCreateDto createDto
    ) {
        BudgetsDto budgetsDto = budgetService.createOrUpdate(member, createDto);
        return ResponseEntity.ok(budgetsDto);
    }

    @GetMapping
    public ResponseEntity<BudgetsDto> getBudget(
            @AuthenticationPrincipal Member member
    ) {
        BudgetsDto budgetsDto = budgetService.getBudgets(member);
        return ResponseEntity.ok(budgetsDto);
    }

    @GetMapping("/recommend")
    public ResponseEntity<BudgetsDto> recommendBudget(
            @AuthenticationPrincipal Member member,
            @RequestParam(value = "total_amount") Integer totalAmount
    ) {
        BudgetsDto budgetsDto = budgetService.recommend(totalAmount);
        return ResponseEntity.ok(budgetsDto);
    }
}
