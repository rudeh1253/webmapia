package nsl.webmapia.game.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Web Mapia API Specification",
                description = "Web Mapia uses REST API as a part of the system, so API specification is needed."
        )
)
@Configuration
public class WebConfig {
}
