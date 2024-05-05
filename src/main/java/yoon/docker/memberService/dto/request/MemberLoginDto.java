package yoon.docker.memberService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import yoon.docker.memberService.validation.ValidationGroup;

@Getter
public class MemberLoginDto {

    @Schema(description = "이메일", example = "example@example.com")
    @Email(message = "INVALID_EMAIL_FORMAT", groups = ValidationGroup.EmailFormat.class)
    @NotBlank(message = "EMPTY_EMAIL_FIELD", groups = ValidationGroup.EmailBlank.class)
    @NotNull(message = "EMPTY_EMAIL_FIELD", groups = ValidationGroup.EmailBlank.class)
    private String email;

    @Schema(description = "비밀번호", example = "abcd1234")
    @NotBlank(message = "EMPTY_PASSWORD_FIELD", groups = ValidationGroup.PasswordBlank.class)
    @NotNull(message = "EMPTY_PASSWORD_FIELD", groups = ValidationGroup.PasswordBlank.class)
    private String password;

}
