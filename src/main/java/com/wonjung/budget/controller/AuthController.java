package com.wonjung.budget.controller;

import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.dto.response.TokenDto;
import com.wonjung.budget.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestBody LoginDto loginDto
    ) {
        String accessToken = memberService.login(loginDto);
        return ResponseEntity.ok(new TokenDto(accessToken));
    }
}
