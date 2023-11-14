package com.wonjung.budget.controller;

import com.wonjung.budget.dto.response.ExpenseRecommendDto;
import com.wonjung.budget.dto.response.FeedbackDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.ConsultingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ConsultingController {

    private final ConsultingService consultingService;

    @GetMapping("/todays-feedback")
    public ResponseEntity<FeedbackDto> getFeedbackExpenseOfToday(
            @AuthenticationPrincipal Member member
    ) {
        FeedbackDto feedbackDto = consultingService.getFeedbackExpenseOfToday(member);
        return ResponseEntity.ok(feedbackDto);
    }

    @GetMapping("/todays-recommend")
    public ResponseEntity<ExpenseRecommendDto> getRecommendExpenseOfToday(
            @AuthenticationPrincipal Member member
    ) {
        ExpenseRecommendDto recommendDto = consultingService.getRecommendExpenseOfToday(member);
        return ResponseEntity.ok(recommendDto);
    }
}
