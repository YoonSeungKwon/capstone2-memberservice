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
import org.springframework.web.multipart.MultipartFile;
import yoon.docker.memberService.dto.request.MailDto;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberRegisterDto;
import yoon.docker.memberService.dto.request.MemberUpdateDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.service.MemberService;
import yoon.docker.memberService.validation.LoginValidationSequence;
import yoon.docker.memberService.validation.RegisterValidationSequence;
import yoon.docker.memberService.validation.UpdateValidationSequence;

import java.util.List;

@Tag(name="멤버관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "관리자용 멤버 리스트 불러오기")
    @GetMapping("/lists")
    public ResponseEntity<List<MemberResponse>> getAllMembers(){

        List<MemberResponse> result = memberService.getMembersList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "관리자용 멤버 리스트 불러오기")
    @PostMapping("/lists")
    public ResponseEntity<List<MemberResponse>> getFriendMembersList(@RequestBody List<Long> idxList){

        List<MemberResponse> result = memberService.getFriendList(idxList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "관리자용 멤버 개인 불러오기")
    @GetMapping("/{idx}")
    public ResponseEntity<MemberResponse> getMemberByIdx(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 인덱스", required = true) @PathVariable long idx){

        MemberResponse result = memberService.getMember(idx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "이메일 중복 체크")
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody String email){

        boolean result = memberService.emailCheck(email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 확인")
    @PostMapping("/auth")
    public ResponseEntity<Boolean> checkPassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 로그인 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberLoginDto.class)))
            @RequestBody MemberLoginDto dto){

        boolean result = memberService.checkPassword(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> memberRegister(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 회원가입 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberRegisterDto.class)))
            @RequestBody @Validated(RegisterValidationSequence.class) MemberRegisterDto dto){

        MemberResponse response = memberService.register(dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> memberLogin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 로그인 정보", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberLoginDto.class)))
            @RequestBody @Validated(LoginValidationSequence.class) MemberLoginDto dto, HttpServletResponse response) {

        MemberResponse result = memberService.login(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "회원 정보 수정 비밀번호")
    @PutMapping("/{idx}")
    public ResponseEntity<MemberResponse> updatePassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "비밀 번호", required = true, content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = MemberUpdateDto.class)))
            @PathVariable long idx, @RequestBody @Validated(UpdateValidationSequence.class) MemberUpdateDto dto){

        MemberResponse result = memberService.updatePassword(idx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "친구 추가를 위해 이메일로 유저 검색")
    @GetMapping("/search/{email}")
    public ResponseEntity<MemberResponse> searchFriend(@PathVariable String email){

        MemberResponse result = memberService.searchMemberByEmail(email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "사용자 프로필 변경")
    @PostMapping("/profile")
    public ResponseEntity<MemberResponse> updateProfile(@RequestParam("file") MultipartFile file){

        MemberResponse result = memberService.updateProfile(file);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "닉네임 변경")
    @PatchMapping("/name")
    public ResponseEntity<MemberResponse> updateUsername(@RequestBody String name){

        MemberResponse result = memberService.updateName(name);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "관리자용 유저 삭제")
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMember(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "유저 인덱스", required = true) @PathVariable long idx){

        memberService.deleteMember(idx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/mail/auth")
    public ResponseEntity<Boolean> mail(@RequestBody MailDto dto){


        boolean result = memberService.sendMail(dto.getEmail());

        if(result){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/mail/auth")
    public ResponseEntity<Boolean> check(@RequestBody MailDto dto){

        boolean result = memberService.checkMail(dto);

        if(result){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }


}
