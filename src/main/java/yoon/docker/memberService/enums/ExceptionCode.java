package yoon.docker.memberService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //회원가입 에러코드

    INVALID_EMAIL_FORMAT("잘못된 이메일 형식.", HttpStatus.BAD_REQUEST),

    EMPTY_EMAIL_FIELD("이메일을 입력해 주세요.", HttpStatus.BAD_REQUEST),

    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일 주소입니다.", HttpStatus.BAD_REQUEST),

    EMAIL_CHECK_REQUIRED("이메일 중복확인을 해주세요.", HttpStatus.BAD_REQUEST),

    EMPTY_PASSWORD_FIELD("비밀번호를 입력해 주세요.", HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD_LENGTH("비밀번호는 최소 8자리 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    EMPTY_USERNAME_FIELD("이름을 입력해 주세요.", HttpStatus.BAD_REQUEST),

    EMPTY_PHONE_NUMBER("전화번호를 입력해 주세요.", HttpStatus.BAD_REQUEST),

    INVALID_PHONE_NUMBER("전화번호를 올바르게 입력해 주세요.", HttpStatus.BAD_REQUEST),

    //유틸 에러

    NOT_IMAGE_FORMAT("파일의 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    FILE_SIZE_EXCEEDED( "10MB 이하의 파일만 업로드 할 수 있습니다.", HttpStatus.BAD_REQUEST),

    //로그인 에러코드

    EMAIL_NOT_FOUND("존재하지 않는 이메일 주소입니다.", HttpStatus.UNAUTHORIZED),

    INVALID_PASSWORD("이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    INTERNAL_SERVER_ERROR("알 수 없는 에러. 개발자에게 알려주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    //인증 에러
    UNAUTHORIZED_ACCESS("인증되지 않은 접근입니다.", HttpStatus.UNAUTHORIZED),
    ;


    private final String message;

    private final HttpStatus status;

    ExceptionCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
