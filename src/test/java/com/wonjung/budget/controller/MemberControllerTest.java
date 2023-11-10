package com.wonjung.budget.controller;

import com.wonjung.budget.conmmon.ControllerTestWithAuth;
import com.wonjung.budget.dto.request.MemberCreateDto;
import com.wonjung.budget.dto.request.MemberEditDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.exception.ErrorCode;
import com.wonjung.budget.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestWithAuth {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 - 성공")
    public void signup() throws Exception {
        //given
        MemberCreateDto request = new MemberCreateDto("test1234", "test123456!", "member1", true);

        //when & then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 - 중복 계정")
    public void signup_duplicate_account() throws Exception {
        //given
        memberRepository.save(Member.builder()
                        .account("test1234")
                        .password("$2a$10$MrzeO291MIulxgrV0N81duNdI8sKKucGqcLvTcbSQTnjgy/CCZGbK")
                        .nickname("member1")
                .build());

        MemberCreateDto createDto = new MemberCreateDto("test1234", "test123456!", "test1", true);

        //when & then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.DUPLICATE_ACCOUNT.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 - 비밀번호 형식 오류")
    public void signup_invalid_password() throws Exception {
        //given
        MemberCreateDto createDto = new MemberCreateDto("test1234", "test1234", "test1", true);

        //when & then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.INVALID_REQUEST.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 정보 조회 - 성공")
    public void get_member_detail() throws Exception {
        //when & then
        mockMvc.perform(get("/api/members")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(memberAccount))
                .andExpect(jsonPath("$.nickname").value(memberNickname))
                .andExpect(jsonPath("$.push_option").value(memberPushOption))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 정보 수정 - 성공")
    public void edit_member_detail() throws Exception {
        // given
        MemberEditDto editDto = new MemberEditDto("테스트 사용자 닉네임 수정", null);

        //when & then
        mockMvc.perform(put("/api/members")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(memberAccount))
                .andExpect(jsonPath("$.nickname").value("테스트 사용자 닉네임 수정"))
                .andExpect(jsonPath("$.push_option").value(memberPushOption))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 정보 수정 - 닉네임 값 누락")
    public void edit_member_detail_fail_no_nickname() throws Exception {
        // given
        MemberEditDto editDto = new MemberEditDto("", false);

        //when & then
        mockMvc.perform(put("/api/members")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(ErrorCode.INVALID_REQUEST.name()))
                .andDo(print());
    }
    
    
}