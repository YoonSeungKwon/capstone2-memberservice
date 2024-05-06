package yoon.docker.memberService;

import jakarta.persistence.LockTimeoutException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yoon.docker.memberService.controller.MemberController;
import yoon.docker.memberService.dto.request.RegisterDto;
import yoon.docker.memberService.dto.request.MemberTestDto;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.enums.Role;
import yoon.docker.memberService.repository.MemberRepository;
import yoon.docker.memberService.security.jwt.JwtProvider;
import yoon.docker.memberService.service.MemberService;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    @Transactional
    void Duplicate_Email_Test() throws Exception {

        Members members = Members.builder()
                .email("test1@test.com")
                .password("12345678")
                .name("test")
                .phone("010-1234-5678")
                .role(Role.USER)
                .build();

        memberRepository.save(members);


        //이메일 중복 체크
        Assertions.assertThat(memberService.emailCheck("test1@test.com"))
                .isTrue();

        //동일한 이메일 가입 불가
        mockMvc.perform(post("/api/v1/members/register")
                .with(csrf())
                .content("{\"email\":\"test1@test.com\",\"password\":\"12345678\",\"username\":\"test\",\"phone\":\"010-1234-5678\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    @Transactional
    void Member_Thread_Test() throws Exception {

        int executeNum = 100;

        AtomicInteger successCount = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(executeNum);

        for(int i=0; i<executeNum; i++){
            executorService.execute(()->{
                try{
                    RegisterDto dto = new MemberTestDto("test2@test.com", "12345678", "tester", "010-1234-5678");
                    memberService.register(dto);
                    successCount.getAndIncrement();
                    System.out.println("Success");
                } catch (LockTimeoutException e){
                    System.out.println("Lock Timeout");
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        Assertions.assertThat(successCount.get()).isEqualTo(1);

        List<Members> list = memberRepository.findAll();
        for(int i=0;  i<list.size(); i++){
            System.out.println(list.get(i).getEmail());
        }
    }


}
