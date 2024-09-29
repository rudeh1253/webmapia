package nsl.webmapia.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configuration class for STOMP-based chatting application.
 * @author PGD
 */
@Configuration
@EnableWebSocketMessageBroker
@Profile("sock-js")
public class SockJsWebSocketMessageBrokerConfig extends StandardWebSocketMessageBrokerConfig {

    @Value("${client.origin}")
    private String clientOrigin;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/game-service").setAllowedOrigins(this.clientOrigin).withSockJS();
    }
}
