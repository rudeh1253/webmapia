package nsl.webmapia.game.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * Configuration class for STOMP-based chatting application.
 * @author PGD
 */
@Slf4j
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

    @EventListener
    public void onConnected(SessionConnectedEvent event) {
        log.info("source={}", event.getSource());
        log.info("timestamp={}", event.getTimestamp());
    }
}
