package yoon.docker.memberService.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberUpdateDto;
import yoon.docker.memberService.dto.request.RegisterDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.entity.Members;
import yoon.docker.memberService.enums.ExceptionCode;
import yoon.docker.memberService.enums.Role;
import yoon.docker.memberService.exception.UnAuthorizedException;
import yoon.docker.memberService.exception.UtilException;
import yoon.docker.memberService.repository.MemberRepository;
import yoon.docker.memberService.security.jwt.JwtProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AmazonS3Client amazonS3Client;

    private final String DEFAULT_PROFILE = "https://pinkok-storage.s3.ap-northeast-2.amazonaws.com/members/default.png";
    private final String bucket = "pinkok-storage";
    private final String region = "ap-northeast-2";

    private MemberResponse toResponse(Members members) {
        return new MemberResponse(members.getMemberIdx(), members.getEmail(), members.getUsername()
                , members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public boolean emailCheck(String email) {
        return memberRepository.existsMembersByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(MemberLoginDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        return passwordEncoder.matches(password, memberRepository.findMembersByEmail(email).getPassword());
    }

    @Transactional(readOnly = true)
    public MemberResponse searchMemberByEmail(String email) {

        Members members = memberRepository.findMembersByEmail(email);
        if (members == null)
            throw new UsernameNotFoundException(email);

        return toResponse(members);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(long idx) {
        Members members = memberRepository.findMembersByMemberIdx(idx);

        if (members == null)
            throw new UsernameNotFoundException(String.valueOf(idx));

        return toResponse(members);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getMembersList() {
        List<Members> list = memberRepository.findAll();
        List<MemberResponse> result = new ArrayList<>();

        for (Members m : list) {
            result.add(toResponse(m));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getFriendList(List<Long> list) {
        List<Members> members = memberRepository.findMembersByMemberIdxIn(list);
        List<MemberResponse> response = new ArrayList<>();
        for (Members m : members) {
            response.add(toResponse(m));
        }
        return response;
    }

    @Transactional
    public MemberResponse register(RegisterDto dto) {

        if (memberRepository.existsMembersByEmail(dto.getEmail()))//이미 존재하는 이메일 주소
            throw new UnAuthorizedException(ExceptionCode.EMAIL_ALREADY_EXISTS.getMessage(), ExceptionCode.EMAIL_ALREADY_EXISTS.getStatus());

        Members members = Members.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getUsername())
                .phone(dto.getPhone())
                .role(Role.USER)
                .build();

        members.setProfile(DEFAULT_PROFILE);

        return toResponse(memberRepository.save(members));
    }

    @Transactional
    public MemberResponse login(MemberLoginDto dto, HttpServletResponse response) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Members members = memberRepository.findMembersByEmail(email);

        if (members == null)
            throw new UsernameNotFoundException(email);
        if (!passwordEncoder.matches(password, members.getPassword()))
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
    public MemberResponse updatePassword(long idx, MemberUpdateDto dto) {
        String password = dto.getPassword();

        Members members = memberRepository.findMembersByMemberIdx(idx);

        members.setPassword(passwordEncoder.encode(password));

        return toResponse(members);
    }

    @Transactional
    public void deleteMember(long idx) {

        memberRepository.deleteByMemberIdx(idx);

    }

    @Transactional
    public void updateProfile(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();

        String url;
        UUID uuid = UUID.randomUUID();

        if (!file.getContentType().startsWith("image")) {
            throw new UtilException(ExceptionCode.NOT_IMAGE_FORMAT.getMessage(), ExceptionCode.NOT_IMAGE_FORMAT.getStatus());
        }
        try {
            String fileName = uuid + file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/members/" + currentMember.getMemberIdx() + "/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            System.out.println(file.getContentType());
            url = fileUrl;
            amazonS3Client.putObject(bucket + "/members/" + currentMember.getMemberIdx(), fileName, file.getInputStream(), objectMetadata);

            String currentProfileUrl = currentMember.getProfile();

            if (currentProfileUrl != null && !currentProfileUrl.isEmpty() && !currentProfileUrl.equals(DEFAULT_PROFILE)) {
                String existingFileKey = currentProfileUrl.substring(currentProfileUrl.indexOf(".com/") + 5);
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, existingFileKey));
            }

            currentMember.setProfile(url);
            memberRepository.save(currentMember);

        } catch (Exception e) {
            throw new UtilException(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
        }


    }

}
