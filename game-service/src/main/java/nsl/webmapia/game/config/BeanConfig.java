package nsl.webmapia.game.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
