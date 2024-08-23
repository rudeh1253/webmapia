package nsl.webmapia.game.config;

import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.InMemoryGameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.InMemoryPhaseEndRequestRepository;
import nsl.webmapia.game.gameoperation.repository.PhaseEndRequestRepository;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.InMemoryGameRoomRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class defines Spring Beans.
 */
@Configuration
public class BeanConfig {

    @Bean
    @ConditionalOnMissingBean(GameRoomRepository.class)
    public GameRoomRepository inMemoryGameRoomRepository() {
        return new InMemoryGameRoomRepository();
    }

    @Bean
    @ConditionalOnMissingBean(GameInstanceRepository.class)
    public InMemoryGameInstanceRepository inMemoryGameInstanceRepository() {
        return new InMemoryGameInstanceRepository();
    }

    @Bean
    @ConditionalOnMissingBean(PhaseEndRequestRepository.class)
    public InMemoryPhaseEndRequestRepository inMemoryPhaseEndRequestRepository() {
        return new InMemoryPhaseEndRequestRepository();
    }
}
