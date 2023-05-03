package com.api_blog.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;
 
@Configuration
@OpenAPIDefinition(info = @Info(title = "BLOG_API | BACKEND PROJECT",description = "Spring Boot Blog_API Backend Project", version = "0.0.1",
        contact = @Contact(name = "Manas Ranjan Singh", email = "manassingh900@gmail.com")),
        security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT",in = SecuritySchemeIn.HEADER )
})
public class OpenApiConfig {
}
