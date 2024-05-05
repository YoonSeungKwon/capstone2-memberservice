package yoon.docker.memberService.unitTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import yoon.docker.memberService.controller.MemberController;
import yoon.docker.memberService.enums.ExceptionCode;
import yoon.docker.memberService.service.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@WithMockUser("tester")
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;


    @Test
    void Register_Validation_Test()throws Exception{


        String url = "/api/v1/members/register";


        //이메일 비어있음
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\",\"password\":\"\",\"username\":\"\",\"phone\":\"\"}")
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_EMAIL_FIELD.getMessage()));

        //이메일 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_EMAIL_FIELD.getMessage()));

        //이메일 형식 오류
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test\",\"password\":\"\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.INVALID_EMAIL_FORMAT.getMessage()));


        //비밀번호 비어있음
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PASSWORD_FIELD.getMessage()));

        //비밀번호 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PASSWORD_FIELD.getMessage()));

        //비밀번호 8자리
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"1234567\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.INVALID_PASSWORD_LENGTH.getMessage()));

        //이름 Blank
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"username\":\"\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_USERNAME_FIELD.getMessage()));

        //이름 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_USERNAME_FIELD.getMessage()));

        //휴대번호 Blank
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"username\":\"test\",\"phone\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PHONE_NUMBER.getMessage()));

        //휴대번호 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"username\":\"test\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PHONE_NUMBER.getMessage()));

        //휴대번호 길이
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"username\":\"test\",\"phone\":\"12345678\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.INVALID_PHONE_NUMBER.getMessage()));


    }

    @Test
    void Login_Validation_Test()throws Exception{

        String url = "/api/v1/members/login";

        //이메일 비어있음
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"password\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_EMAIL_FIELD.getMessage()));

        //이메일 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_EMAIL_FIELD.getMessage()));

        //이메일 형식 오류
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test\",\"password\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.INVALID_EMAIL_FORMAT.getMessage()));

        //비밀번호 비어있음
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"password\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PASSWORD_FIELD.getMessage()));

        //비밀번호 Null
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1@test.com\",\"username\":\"\"}")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ExceptionCode.EMPTY_PASSWORD_FIELD.getMessage()));

    }
}
