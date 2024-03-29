package yoon.docker.memberService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoon.docker.memberService.enums.ErrorCode;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<String> UserNameNotFoundError(){
        ErrorCode code = ErrorCode.EMAIL_NOT_FOUND;
        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> BadCredentialError(){
        ErrorCode code = ErrorCode.INVALID_PASSWORD;
        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> ValidationException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        String error = bindingResult.getAllErrors().get(0).getDefaultMessage();

        ErrorCode code = switch (Objects.requireNonNull(error)) {
            case "INVALID_EMAIL_FORMAT" -> ErrorCode.INVALID_EMAIL_FORMAT;

            case "EMPTY_EMAIL_FIELD" -> ErrorCode.EMPTY_EMAIL_FIELD;

            case "EMPTY_PASSWORD_FIELD" -> ErrorCode.EMPTY_PASSWORD_FIELD;

            case "INVALID_PASSWORD_LENGTH" -> ErrorCode.INVALID_PASSWORD_LENGTH;

            case "EMPTY_USERNAME_FIELD" -> ErrorCode.EMPTY_USERNAME_FIELD;

            case "EMPTY_PHONE_NUMBER" -> ErrorCode.EMPTY_PHONE_NUMBER;

            case "INVALID_PHONE_NUMBER" -> ErrorCode.INVALID_PHONE_NUMBER;

            default -> ErrorCode.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }


}
