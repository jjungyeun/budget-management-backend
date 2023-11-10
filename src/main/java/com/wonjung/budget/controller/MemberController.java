package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> signup(
            @RequestBody MemberCreateDto createDto
    ) {
        Long createdId = memberService.signup(createDto);
        return ResponseEntity.ok(createdId);
    }
}
