package yoon.docker.memberService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleAccount {

    private String email;

    private String name;

    private String profile;

}
