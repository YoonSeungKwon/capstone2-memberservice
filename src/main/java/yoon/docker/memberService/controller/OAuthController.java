package yoon.docker.memberService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import yoon.docker.memberService.dto.response.KakaoResponse;
import yoon.docker.memberService.service.OAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/callback/kakao")
    public void kakaoAuth(@RequestParam("code") String code){
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", "62f0d478f17a50a9a77a46a7b150ec16");
        body.add("redirect_uri", "http://localhost:3000/api/v1/callback/kakao");
        body.add("code", code);
        body.add("client_secret", "uC3reFcRCqNdLooSG0zz9jPgGhFWIHjl");

        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(body, headers);

        System.out.println(code);

        ResponseEntity<String> response = template.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoRequest, String.class);

        System.out.println(response);
    }



}
