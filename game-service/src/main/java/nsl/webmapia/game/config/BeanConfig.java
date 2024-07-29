package nsl.webmapia.game.config;

import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.InMemoryGameRoomRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class defines Spring Beans.
 */
@Configuration
public class BeanConfig {

    @ConditionalOnMissingBean
    public GameRoomRepository inMemoryGameRoomRepository() {
        return new InMemoryGameRoomRepository();
    }
}
