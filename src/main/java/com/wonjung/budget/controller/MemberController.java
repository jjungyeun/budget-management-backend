package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.dto.response.MemberDetailDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.security.AuthenticationPrincipal;
import com.wonjung.budget.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> signup(
            @Valid @RequestBody MemberCreateDto createDto
    ) {
        Long createdId = memberService.signup(createDto);
        return ResponseEntity.ok(createdId);
    }

    @GetMapping
    public ResponseEntity<MemberDetailDto> getMemberDetail(
            @AuthenticationPrincipal Member member
    ) {
        return ResponseEntity.ok(
                new MemberDetailDto(member.getAccount(), member.getNickname(), member.getPushOption()
        ));
    }
}
