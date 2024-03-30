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
import org.springframework.transaction.annotation.Transactional;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberRegisterDto;
import yoon.docker.memberService.dto.request.MemberUpdateDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.enums.Role;
import yoon.docker.memberService.repository.MemberRepository;
import yoon.docker.memberService.security.jwt.JwtProvider;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private MemberResponse toResponse(Members members){
        return new MemberResponse(members.getMemberIdx(), members.getEmail(), members.getUsername()
                , members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());
    }

    public boolean emailCheck(String email){
        return memberRepository.existsMembersByEmail(email);
    }

    public boolean checkPassword(MemberLoginDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();

        return passwordEncoder.matches(password, memberRepository.findMembersByEmail(email).getPassword());
    }

    public MemberResponse getMember(long idx){
        Members members = memberRepository.findMembersByMemberIdx(idx);

        if(members == null)
            throw new UsernameNotFoundException(String.valueOf(idx));

        return toResponse(members);
    }

    public List<MemberResponse> getMembersList(){
        List<Members> list = memberRepository.findAll();
        List<MemberResponse> result = new ArrayList<>();

        for(Members m : list){
            result.add(toResponse(m));
        }
        return result;
    }

    @Transactional
    public MemberResponse register(MemberRegisterDto dto){

        Members members = Members.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getUsername())
                .phone(dto.getPhone())
                .role(Role.USER)
                .build();

        return toResponse(memberRepository.save(members));
    }

    @Transactional
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

    @Transactional
    public MemberResponse updatePassword(long idx, MemberUpdateDto dto){
        String password = dto.getPassword();

        Members members = memberRepository.findMembersByMemberIdx(idx);

        members.setPassword(passwordEncoder.encode(password));

        return toResponse(members);
    }

    @Transactional
    public MemberResponse updateProfile(long idx, String profile){

        Members members = memberRepository.findMembersByMemberIdx(idx);

        members.setProfile(profile);

        return toResponse(members);
    }

    @Transactional
    public void deleteMember(long idx){

        memberRepository.deleteByMemberIdx(idx);

        return;
    }

}
