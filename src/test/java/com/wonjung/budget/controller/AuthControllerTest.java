package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTest;
import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 - 성공")
    public void login() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .account("test1234")
                .password("$2a$10$MrzeO291MIulxgrV0N81duNdI8sKKucGqcLvTcbSQTnjgy/CCZGbK")
                .nickname("test1")
                .build());

        LoginDto loginDto = new LoginDto("test1234", "test123456!");

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 실패(잘못된 비밀번호)")
    public void login_invalid_password() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .account("test1234")
                .password("$2a$10$MrzeO291MIulxgrV0N81duNdI8sKKucGqcLvTcbSQTnjgy/CCZGbK")
                .nickname("test1")
                .build());

        LoginDto request = new LoginDto("test1234", "wrongPassword");

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.LOGIN_FAILED.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 실패(존재하지 않는 계정)")
    public void login_invalid_account() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .account("test1234")
                .password("$2a$10$MrzeO291MIulxgrV0N81duNdI8sKKucGqcLvTcbSQTnjgy/CCZGbK")
                .nickname("test1")
                .build());

        LoginDto request = new LoginDto("test1234567", "wrongPassword");

        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.LOGIN_FAILED.name()))
                .andDo(print());
    }

}