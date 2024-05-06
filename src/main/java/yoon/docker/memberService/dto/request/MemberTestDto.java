package yoon.docker.memberService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTestDto implements RegisterDto {

    private String email;

    private String username;

    private String password;

    private String phone;

}
