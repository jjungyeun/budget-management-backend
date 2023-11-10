package com.wonjung.budget.service.impl;

import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.dto.request.MemberEditDto;
import com.wonjung.budget.dto.response.MemberDetailDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.MemberRepository;
import com.wonjung.budget.security.JwtUtils;
import com.wonjung.budget.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils tokenProvider;

    @Transactional
    @Override
    public Long signup(MemberCreateDto createDto) {
        if (isDuplicatedAccount(createDto.account())) {
            throw new CustomException(ErrorCode.DUPLICATE_ACCOUNT);
        }

        String encryptedPassword = passwordEncoder.encode(createDto.password());

        Member member = memberRepository.save(
                Member.builder()
                        .account(createDto.account())
                        .password(encryptedPassword)
                        .nickname(createDto.nickname())
                        .pushOption(createDto.pushOption())
                        .build()
        );

        return member.getId();
    }

    @Override
    public String login(LoginDto loginDto) {
        Member member = memberRepository.findByAccount(loginDto.account())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(loginDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return tokenProvider.createToken(member.getId());
    }

    @Transactional
    @Override
    public MemberDetailDto editMember(Member member, MemberEditDto editDto) {
        member.edit(editDto.nickname(), editDto.pushOption());
        return MemberDetailDto.builder()
                .account(member.getAccount())
                .nickname(member.getNickname())
                .pushOption(member.getPushOption())
                .build();
    }

    private boolean isDuplicatedAccount(String account) {
        return memberRepository.findByAccount(account).isPresent();
    }
}
