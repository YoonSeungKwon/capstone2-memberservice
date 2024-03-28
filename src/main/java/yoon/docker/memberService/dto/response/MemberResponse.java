package yoon.docker.memberService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private String email;

    private String name;

    private String profile;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}