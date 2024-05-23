package yoon.docker.memberService.dto.response;

import lombok.Builder;
import lombok.Getter;
import yoon.docker.memberService.enums.Provider;

import java.util.Map;

@Getter
public class OAuthAttribute {

    private String email;

    private String name;

    private Provider provider;

    private String attributeKey;

    private Map<String, Object> attributes;

    @Builder
    OAuthAttribute(String email, String name, Provider provider, String attributeKey, Map<String, Object> attributes){
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.attributeKey = attributeKey;
        this.attributes = attributes;
    }

}
