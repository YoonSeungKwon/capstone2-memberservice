package yoon.docker.memberService.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberRegisterDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.enums.Role;
import yoon.docker.memberService.repository.MemberRepository;
import yoon.docker.memberService.security.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private MemberResponse toResponse(Members members){
        return new MemberResponse(members.getEmail(), members.getUsername(), members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());
    }

    public MemberResponse register(MemberRegisterDto dto){

        //Validation Need
        if(dto.getEmail() == null || dto.getPassword() == null || dto.getName() == null)
            throw new RuntimeException();
        if(memberRepository.existsMembersByEmail(dto.getEmail()))
            throw new RuntimeException();
        //

        Members members = Members.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .role(Role.USER)
                .build();
        return toResponse(memberRepository.save(members));
    }

    public MemberResponse login(MemberLoginDto dto, HttpServletResponse response){
        String email = dto.getEmail();
        String password = dto.getPassword();
        Members members = memberRepository.findMembersByEmail(email);

        if(members == null)
            throw new UsernameNotFoundException(email);
        if(!passwordEncoder.matches(password, members.getPassword()))
            throw new BadCredentialsException(email);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                members, null, members.getAuthority()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accToken = jwtProvider.createAccessToken(email);
        String refToken = jwtProvider.createRefreshToken();

        response.setHeader("Authorization", accToken);
        response.setHeader("X-Refresh-Token", refToken);


        members.setRefresh(refToken);

        return toResponse(memberRepository.save(members));
    }


}
