package yoon.docker.memberService.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.service.OAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oAuthService;


    @GetMapping("/callback/kakao")
    public ResponseEntity<MemberResponse> kakaoAuth(@RequestParam("code") String code, HttpServletResponse response){

        MemberResponse result = oAuthService.kakaoLogin(code, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/callback/naver")
    public ResponseEntity<MemberResponse> naverAuth(@RequestParam("code") String code, HttpServletResponse response){

        MemberResponse result = oAuthService.naverLogin(code, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/callback/google")
    public ResponseEntity<MemberResponse> googleAuth(@RequestParam("code") String code, HttpServletResponse response){

        MemberResponse result = oAuthService.googleLogin(code, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
