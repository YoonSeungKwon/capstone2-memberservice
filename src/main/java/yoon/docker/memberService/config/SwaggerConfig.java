package yoon.docker.memberService.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi memberApiGroup(){
        return GroupedOpenApi.builder()
                .group("MEMBER API")
                .pathsToMatch("/api/v1/members/**")
                .build();
    }
}
