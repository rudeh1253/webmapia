package nsl.webmapia.game.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(
        info = @Info(
                title = "Web Mapia API Specification",
                description = "Web Mapia uses REST API as a part of the system, so API specification is needed."
        )
)
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Value("${client.origin}")
        private String clientOrigin;

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(this.clientOrigin)
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS")
                        .maxAge(3600);
        }
}
