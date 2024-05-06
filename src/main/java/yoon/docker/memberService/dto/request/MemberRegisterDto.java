package yoon.docker.memberService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import yoon.docker.memberService.validation.ValidationGroup;

@Getter
public class MemberRegisterDto implements RegisterDto {

    @Schema(description = "이메일", example = "example@example.com")
    @Email(message = "INVALID_EMAIL_FORMAT", groups = ValidationGroup.EmailFormat.class)
    @NotBlank(message = "EMPTY_EMAIL_FIELD", groups = ValidationGroup.EmailBlank.class)
    @NotNull(message = "EMPTY_EMAIL_FIELD", groups = ValidationGroup.EmailBlank.class)
    private String email;

    @Schema(description = "비밀번호 8자리 이상", example = "abcd1234")
    @NotBlank(message = "EMPTY_PASSWORD_FIELD", groups = ValidationGroup.PasswordBlank.class)
    @NotNull(message = "EMPTY_PASSWORD_FIELD", groups = ValidationGroup.PasswordBlank.class)
    @Length(min = 8, message = "INVALID_PASSWORD_LENGTH", groups = ValidationGroup.PasswordFormat.class)
    private String password;

    @Schema(description = "이름", example = "홍길동")
    @NotBlank(message = "EMPTY_USERNAME_FIELD", groups = ValidationGroup.NameBlank.class)
    @NotNull(message = "EMPTY_USERNAME_FIELD", groups = ValidationGroup.NameBlank.class)
    private String username;

    @Schema(description = "휴대 전화 번호 13자리", example = "010-1234-5678")
    @NotBlank(message = "EMPTY_PHONE_NUMBER", groups = ValidationGroup.PhoneBlank.class)
    @NotNull(message = "EMPTY_PHONE_NUMBER", groups = ValidationGroup.PhoneBlank.class)
    @Size(min = 13, max = 13, message = "INVALID_PHONE_NUMBER", groups = ValidationGroup.PhoneFormat.class)
    private String phone;

}
