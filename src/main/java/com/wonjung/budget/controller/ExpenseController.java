package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.ExpenseCreateDto;
import com.wonjung.budget.dto.response.ExpenseDetailDto;
import com.wonjung.budget.dto.response.ExpensesResDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.ExpenseService;
import com.wonjung.budget.type.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping
    public ResponseEntity<ExpensesResDto> getExpenses(
            @AuthenticationPrincipal Member member,
            @RequestParam(value = "start_date") LocalDate startDate,
            @RequestParam(value = "end_date") LocalDate endDate,
            @RequestParam(value = "min_amount", required = false, defaultValue = "0") Integer minAmount,
            @RequestParam(value = "max_amount", required = false) Integer maxAmount,
            @RequestParam(value = "order_by", required = false, defaultValue = "date:desc") String orderBy,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String categoryList
    ) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        if (maxAmount == null) {
            maxAmount = Integer.MAX_VALUE;
        }

        if (minAmount > maxAmount) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        OrderStatus orderStatus = OrderStatus.parse(orderBy);
        String[] categories = categoryList == null || categoryList.isBlank()?
                new String[]{} : categoryList.split(",");

        ExpensesResDto expenses = expenseService.getExpenses(member, startDate, endDate, minAmount, maxAmount, orderStatus, search, categories);
        return ResponseEntity.ok(expenses);
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
