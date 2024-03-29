package yoon.docker.memberService.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({
        ValidationGroup.EmailBlank.class,
        ValidationGroup.EmailFormat.class,
        ValidationGroup.EmailCheck.class,
        ValidationGroup.PasswordBlank.class,
        ValidationGroup.PasswordFormat.class,
        ValidationGroup.NameBlank.class,
        ValidationGroup.PhoneBlank.class,
        ValidationGroup.PhoneFormat.class,
})
public interface RegisterValidationSequence {
}
