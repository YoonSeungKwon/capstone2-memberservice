package yoon.docker.memberService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberRegisterDto;
import yoon.docker.memberService.dto.request.MemberUpdateDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.service.MemberService;
import yoon.docker.memberService.validation.LoginValidationSequence;
import yoon.docker.memberService.validation.RegisterValidationSequence;
import yoon.docker.memberService.validation.UpdateValidationSequence;

import java.util.List;
import java.util.Map;

@Tag(name="멤버관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;


    @Operation(description = "관리자용 멤버 리스트 불러오기")
    @GetMapping("/lists")
    public ResponseEntity<List<MemberResponse>> getMembersList(){

        List<MemberResponse> result = memberService.getMembersList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "관리자용 멤버 리스트 불러오기")
    @PostMapping("/lists")
    public ResponseEntity<List<MemberResponse>> getFriendMembersList(@RequestBody List<Long> idxList){

        List<MemberResponse> result = memberService.getFriendList(idxList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "관리자용 멤버 개인 불러오기")
    @GetMapping("/{idx}")
    public ResponseEntity<MemberResponse> getMembersList(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 인덱스", required = true) @PathVariable long idx){

        MemberResponse result = memberService.getMember(idx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "이메일 중복 체크")
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody String email){

        boolean result = memberService.emailCheck(email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "비밀번호 확인")
    @PostMapping("/auth")
    public ResponseEntity<Boolean> checkPassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 로그인 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberLoginDto.class)))
            @RequestBody MemberLoginDto dto){

        boolean result = memberService.checkPassword(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> memberRegister(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 회원가입 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberRegisterDto.class)))
            @RequestBody @Validated(RegisterValidationSequence.class) MemberRegisterDto dto){

        MemberResponse response = memberService.register(dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> memberLogin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 로그인 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberLoginDto.class)))
            @RequestBody @Validated(LoginValidationSequence.class) MemberLoginDto dto, HttpServletResponse response) {

        MemberResponse result = memberService.login(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "회원 정보 수정 비밀번호")
    @PutMapping("/{idx}")
    public ResponseEntity<MemberResponse> updatePassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "비밀 번호", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberUpdateDto.class)))
            @PathVariable long idx, @RequestBody @Validated(UpdateValidationSequence.class) MemberUpdateDto dto){

        MemberResponse result = memberService.updatePassword(idx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(description = "회원 정보 수정 프로필 사진")
    @PutMapping("/profile/{idx}")
    public ResponseEntity<MemberResponse> updateProfile(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "아직 스토리지 서버가 없어서 미구현 상태, 문자로 저장 가능", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberUpdateDto.class)))
                                                       @PathVariable long idx, @RequestBody String profile){

        MemberResponse result = memberService.updateProfile(idx, profile);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(description = "관리자용 유저 삭제")
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMember(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 인덱스", required = true) @PathVariable long idx){

        memberService.deleteMember(idx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
