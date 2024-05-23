package yoon.docker.memberService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoon.docker.memberService.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberRepository memberRepository;
}
