package yoon.docker.memberService.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.docker.memberService.dto.request.GoogleAccount;
import yoon.docker.memberService.dto.request.KakaoAccount;
import yoon.docker.memberService.dto.request.NaverAccount;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.service.OAuthService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login/kakao")
    public ResponseEntity<MemberResponse> kakaoAccessLogin(@RequestBody KakaoAccount dto, HttpServletResponse response){

        MemberResponse result = oAuthService.kakaoLogin(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login/naver")
    public ResponseEntity<MemberResponse> naverAccessLogin(@RequestBody NaverAccount dto, HttpServletResponse response){

        MemberResponse result = oAuthService.naverLogin(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login/google")
    public ResponseEntity<MemberResponse> googleAccessLogin(@RequestBody GoogleAccount dto, HttpServletResponse response){

        MemberResponse result = oAuthService.googleLogin(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
