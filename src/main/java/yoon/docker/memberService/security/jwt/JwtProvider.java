package yoon.docker.memberService.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String SECRET;

    @Value("${JWT_ACC}")
    private String ACC;

    @Value("${JWT_REF}")
    private String REF;
    private long getAccExp(){
        return Long.parseLong(ACC) * 60 * 1000l;
    }
    private long getRefExp(){
        return Long.parseLong(REF) * 60 * 60 * 1000l;
    }
    private SecretKey getSecretKey(){
      return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String id){

        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + getAccExp()));

        return  Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HMAC")
                .setClaims(claims)
                .claim("username", id)
                .signWith(getSecretKey())
                .compact();
    }

    public String createRefreshToken(){

        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + getRefExp()));

        return  Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HMAC")
                .setClaims(claims)
                .signWith(getSecretKey())
                .compact();
    }

}
