package com.wonjung.budget.service;

import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.dto.request.MemberCreateDto;

public interface MemberService {
    Long signup(MemberCreateDto createDto);
    String login(LoginDto loginDto);
}
