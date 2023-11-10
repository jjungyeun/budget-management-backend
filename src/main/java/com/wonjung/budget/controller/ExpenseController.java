package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Long> createExpense(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody ExpenseCreateDto createDto
    ) {
            Long createdId = expenseService.create(member, createDto);
            return ResponseEntity.ok(createdId);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDetailDto> getExpenseDetail(
            @AuthenticationPrincipal Member member,
            @PathVariable Long expenseId
    ) {
        ExpenseDetailDto detailDto = expenseService.getDetail(member, expenseId);
        return ResponseEntity.ok(detailDto);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDetailDto> editExpense(
            @AuthenticationPrincipal Member member,
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseCreateDto editDto
    ) {
        ExpenseDetailDto detailDto = expenseService.edit(member, expenseId, editDto);
        return ResponseEntity.ok(detailDto);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ExpenseDetailDto> deleteExpense(
            @AuthenticationPrincipal Member member,
            @PathVariable Long expenseId
    ) {
        expenseService.delete(member, expenseId);
        return ResponseEntity.ok().build();
    }
}
