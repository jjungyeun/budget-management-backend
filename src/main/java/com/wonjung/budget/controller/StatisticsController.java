package com.wonjung.budget.controller;

import com.wonjung.budget.dto.response.ComparisonDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/month")
    public ResponseEntity<ComparisonDto> getComparisonToLastMonth(
            @AuthenticationPrincipal Member member
    ) {
        ComparisonDto comparisonDto = statisticsService.getComparisonToLastMonth(member);
        return ResponseEntity.ok(comparisonDto);
    }

    @GetMapping("/day-of-week")
    public ResponseEntity<ComparisonDto> getComparisonToLastWeek(
            @AuthenticationPrincipal Member member
    ) {
        ComparisonDto comparisonDto = statisticsService.getComparisonToLastWeek(member);
        return ResponseEntity.ok(comparisonDto);
    }
}
