package yoon.docker.memberService.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import yoon.docker.memberService.enums.ExceptionCode;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<String> UserNameNotFoundError(){
        ExceptionCode code = ExceptionCode.EMAIL_NOT_FOUND;
        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> BadCredentialError(){
        ExceptionCode code = ExceptionCode.INVALID_PASSWORD;
        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> ValidationException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        String error = bindingResult.getAllErrors().get(0).getDefaultMessage();

        ExceptionCode code = switch (Objects.requireNonNull(error)) {
            case "INVALID_EMAIL_FORMAT" -> ExceptionCode.INVALID_EMAIL_FORMAT;

            case "EMPTY_EMAIL_FIELD" -> ExceptionCode.EMPTY_EMAIL_FIELD;

            case "EMPTY_PASSWORD_FIELD" -> ExceptionCode.EMPTY_PASSWORD_FIELD;

            case "INVALID_PASSWORD_LENGTH" -> ExceptionCode.INVALID_PASSWORD_LENGTH;

            case "EMPTY_USERNAME_FIELD" -> ExceptionCode.EMPTY_USERNAME_FIELD;

            case "EMPTY_PHONE_NUMBER" -> ExceptionCode.EMPTY_PHONE_NUMBER;

            case "INVALID_PHONE_NUMBER" -> ExceptionCode.INVALID_PHONE_NUMBER;

            default -> ExceptionCode.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<String> unAuthException(UnAuthorizedException e){
        return new ResponseEntity<>(e.getMessage(),e.getStatus());
    }

    @ExceptionHandler({UtilException.class})
    public ResponseEntity<String> utilException(UtilException e){
        return new ResponseEntity<>(e.getMessage(),e.getStatus());
    }

    @ExceptionHandler({FileSizeLimitExceededException.class, SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    public ResponseEntity<String> FileSizeError(){
        return new ResponseEntity<>(ExceptionCode.FILE_SIZE_EXCEEDED.getMessage(), ExceptionCode.FILE_SIZE_EXCEEDED.getStatus());
    }


}
