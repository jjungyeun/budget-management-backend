package com.wonjung.budget.conmmon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.wonjung.budget.dto.request.LoginDto;
import com.wonjung.budget.entity.Member;
import com.wonjung.budget.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class ControllerTestWithAuth extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    protected Long memberId;
    protected String memberAccount = "member_for_test_1";
    protected String memberPassword = "test123456!";
    protected String memberNickname = "테스트용 사용자";
    protected Boolean memberPushOption = true;
    protected String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        Member member = memberRepository.save(Member.builder()
                .account(memberAccount)
                .password("$2a$10$MrzeO291MIulxgrV0N81duNdI8sKKucGqcLvTcbSQTnjgy/CCZGbK")
                .nickname(memberNickname)
                .pushOption(memberPushOption)
                .build());
        memberId = member.getId();

        LoginDto loginDto = new LoginDto(memberAccount, memberPassword);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        accessToken = JsonPath.parse(response).read("$.access_token");
    }
}
