package yoon.docker.memberService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoAccount {

    private String email;

    private String name;

    private String profile;

}