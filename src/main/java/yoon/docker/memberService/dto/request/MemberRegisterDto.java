package yoon.docker.memberService.dto.request;

import lombok.Getter;

@Getter
public class MemberRegisterDto {

    private String email;

    private String password;

    private String name;

    private String phone;

}
