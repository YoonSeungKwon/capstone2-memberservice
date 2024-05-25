package yoon.docker.memberService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UtilException extends RuntimeException{

    private final String message;

    private final HttpStatus status;

    public UtilException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
