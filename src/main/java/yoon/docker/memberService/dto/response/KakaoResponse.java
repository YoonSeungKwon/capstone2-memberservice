package yoon.docker.memberService.dto.response;

import lombok.Getter;

@Getter
public class KakaoResponse {

    private String token_type;

    private String access_token;

    private Integer expires_in;

    private String refresh_token;

    private Integer refresh_token_expires_in;

}
