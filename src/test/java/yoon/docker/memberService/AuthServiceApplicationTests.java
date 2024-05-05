package yoon.docker.memberService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import yoon.docker.memberService.controller.MemberController;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.enums.Role;
import yoon.docker.memberService.repository.MemberRepository;
import yoon.docker.memberService.security.jwt.JwtProvider;
import yoon.docker.memberService.service.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberController memberController;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;


    @Test
    void Duplicate_Email_Test() throws Exception {

        Members members = Members.builder()
                .email("test@test.com")
                .password("12345678")
                .name("test")
                .phone("010-1234-5678")
                .role(Role.USER)
                .build();

        memberRepository.save(members);


        //이메일 중복 체크
        Assertions.assertThat(memberService.emailCheck("test@test.com"))
                .isTrue();

        //동일한 이메일 가입 불가
        mockMvc.perform(post("/api/v1/members/register")
                .with(csrf())
                .content("{\"email\":\"test@test.com\",\"password\":\"12345678\",\"username\":\"test\",\"phone\":\"010-1234-5678\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


}
