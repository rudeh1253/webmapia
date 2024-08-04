package nsl.webmapia.game.config;

import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.gameoperation.repository.InMemoryGameInstanceRepository;
import nsl.webmapia.game.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.gameroom.repository.InMemoryGameRoomRepository;
import nsl.webmapia.game.member.dto.MemberDto;
import nsl.webmapia.game.member.service.MemberService;
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
    @ConditionalOnMissingBean(MemberService.class)
    public MemberService mockMemberService() {
        return new MemberService() {

            @Override
            public MemberDto addMember(MemberDto memberDto) {
                return null;
            }

            @Override
            public MemberDto findMemberById(String memberId) {
                return null;
            }
        };
    }
}
