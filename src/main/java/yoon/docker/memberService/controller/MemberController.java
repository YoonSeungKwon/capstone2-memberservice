package yoon.docker.memberService.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import yoon.docker.memberService.dto.request.MemberLoginDto;
import yoon.docker.memberService.dto.request.MemberRegisterDto;
import yoon.docker.memberService.dto.response.MemberResponse;
import yoon.docker.memberService.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<MemberResponse> memberRegister(@RequestBody MemberRegisterDto dto){

        MemberResponse response = memberService.register(dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> memberLogin(@RequestBody MemberLoginDto dto, HttpServletResponse response){

        MemberResponse result = memberService.login(dto, response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, RuntimeException.class})
    public ResponseEntity<String> errorHandler(Exception e){
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
