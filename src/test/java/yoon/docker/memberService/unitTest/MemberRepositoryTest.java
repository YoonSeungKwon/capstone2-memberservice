package yoon.docker.memberService.unitTest;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.repository.MemberRepository;

import java.util.List;

@DataJpaTest
public class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void Create_Test(){

        Members members = Members.builder()
                .email("test@test.com")
                .password("12345678")
                .name("tester")
                .phone("010-1234-5678")
                .build();

        Assertions.assertThat(memberRepository.save(members))
                .isInstanceOf(Members.class)
                .isEqualTo(members);
    }

    @Test
    @Transactional
    void Read_Test(){

        final int N = 100;

        for(int i=1; i<=N; i++) {
            Members members = Members.builder()
                    .email("test"+i+"@test.com")
                    .password("12345678")
                    .name("tester"+i)
                    .phone("010-1234-5678")
                    .build();

            memberRepository.save(members);
        }


        for(int i=1; i<=N; i++){
            Members members = memberRepository.findMembersByMemberIdx(i);
            Assertions.assertThat(members.getEmail())
                    .isEqualTo("test"+i+"@test.com");
        }

    }

    @Test
    @Transactional
    void Update_Test(){
        final int N = 100;

        for(int i=1; i<=N; i++) {
            Members members = Members.builder()
                    .email("test"+i+"@test.com")
                    .password("12345678")
                    .name("tester"+i)
                    .phone("010-1234-5678")
                    .build();

            memberRepository.save(members);
        }

        for(int i=1; i<=N; i++){
            Members members = memberRepository.findMembersByMemberIdx(i);
            members.setEmail(members.getEmail()+i);
        }


        for(int i=1; i<=N; i++){
            Members members = memberRepository.findMembersByMemberIdx(i);
            Assertions.assertThat(members.getEmail())
                    .isEqualTo("test"+i+"@test.com" + i);
        }

    }

    @Test
    @Transactional
    void Delete_Test(){

        final int N = 100;

        for(int i=1; i<=N; i++) {
            Members members = Members.builder()
                    .email("test"+i+"@test.com")
                    .password("12345678")
                    .name("tester"+i)
                    .phone("010-1234-5678")
                    .build();

            memberRepository.save(members);
        }


        for(int i=1; i<=N; i++){
            memberRepository.deleteByMemberIdx(i);
        }

        List<Members> output = memberRepository.findAll();

        Assertions.assertThat(output)
                .isNullOrEmpty();

    }

}
