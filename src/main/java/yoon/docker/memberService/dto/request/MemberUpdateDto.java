package yoon.docker.memberService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import yoon.docker.memberService.validation.ValidationGroup;

@Getter
public class MemberUpdateDto {

    @Schema(description = "비밀번호 8자리 이상", example = "abcd1234")
    @NotBlank(message = "EMPTY_PASSWORD_FIELD", groups = ValidationGroup.PhoneBlank.class)
    @Length(min = 8, message = "INVALID_PASSWORD_LENGTH", groups = ValidationGroup.PasswordFormat.class)
    private String password;

}
