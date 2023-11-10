package com.wonjung.budget.service;

import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.dto.request.MemberEditDto;
import com.wonjung.budget.dto.response.MemberDetailDto;
import com.wonjung.budget.entity.Member;

public interface MemberService {
    Long signup(MemberCreateDto createDto);
    String login(LoginDto loginDto);
    MemberDetailDto editMember(Member member, MemberEditDto editDto);
}
