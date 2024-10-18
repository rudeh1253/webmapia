package nsl.webmapia.game.domain.gameroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.gameroom.dto.request.GameRoomCreationRequestDto;
import nsl.webmapia.game.domain.gameroom.dto.request.ParticipationRequestDto;
import nsl.webmapia.game.domain.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.domain.gameroom.dto.response.ParticipationResponseDto;
import nsl.webmapia.game.domain.gameroom.entity.GameRoom;
import nsl.webmapia.game.domain.gameroom.repository.GameRoomRepository;
import nsl.webmapia.game.domain.member.dto.MemberDto;
import nsl.webmapia.game.domain.member.entity.Member;
import nsl.webmapia.game.domain.member.repository.MemberRepository;
import nsl.webmapia.game.global.rest.BaseResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestParticipationRestController {

    @LocalServerPort
    int serverPort;

    RestTemplate restTemplate = new RestTemplate();

    GameRoomCreationResponseDto gameRoomCreationResponseDto;

    StompSession hostClientSession;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GameRoomRepository gameRoomRepository;

    static final String SAMPLE_ROOM_NAME = "sample-room";
    static final String SAMPLE_HOST_NAME = "sample-host";

    @BeforeEach
    void beforeEach() throws ExecutionException, InterruptedException, JsonProcessingException {
        this.memberRepository.save(new Member(SAMPLE_HOST_NAME, "samplehost"));
        RequestEntity<GameRoomCreationRequestDto> requestEntity = RequestEntity.post(UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.serverPort)
                        .path("/game/rooms")
                        .toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(GameRoomCreationRequestDto.builder()
                        .creatorId(SAMPLE_HOST_NAME)
                        .roomName(SAMPLE_ROOM_NAME)
                        .build());
        this.gameRoomCreationResponseDto =
                this.objectMapper.readValue(
                        this.restTemplate.exchange(requestEntity, String.class).getBody(),
                        new TypeReference<BaseResponse<GameRoomCreationResponseDto>>() {
                        }
                ).getContent();

        WebSocketStompClient hostClient = new WebSocketStompClient(new StandardWebSocketClient());
        hostClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.hostClientSession =
                hostClient.connectAsync("ws://localhost:" + this.serverPort + "/game-service", new StompSessionHandlerAdapter() {

                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        log.info("sessionId={}", session.getSessionId());
                        log.info("connectedHeaders={}", connectedHeaders);
                    }
                }).get();

        log.info("subscription to={}", "/topic/game-service/rooms/" + this.gameRoomCreationResponseDto.getRoomId());
        this.hostClientSession.subscribe("/topic/game-service/rooms/" + this.gameRoomCreationResponseDto.getRoomId(), new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.info("Host Session - getPayloadType");
                log.info("headers={}", headers);
                return ParticipationResponseDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("Host Session - handleFrame");
                log.info("headers={}", headers);
                log.info("Host Session - Payload = {}", payload);
                ParticipationResponseDto response = (ParticipationResponseDto) payload;
                assertThat(response.getNewParticipant()).isEqualTo("sample-participant");
                assertThat(response.getParticipants().stream().map(MemberDto::getMemberId))
                        .containsExactlyInAnyOrder("sample-participant", SAMPLE_HOST_NAME);
            }
        });
    }

    @AfterEach
    void afterEach() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        PreparedStatement clearParticipationStatement = connection.prepareStatement("""
                DELETE FROM participation
                """);
        PreparedStatement clearGameInstanceStatement = connection.prepareStatement("""
                DELETE FROM game_instance
                """);
        PreparedStatement clearGameRoomStatement = connection.prepareStatement("""
                DELETE FROM game_room
                """);
        PreparedStatement clearMemberStatement = connection.prepareStatement("""
                DELETE FROM members
                """);
        clearParticipationStatement.executeUpdate();
        clearGameInstanceStatement.executeUpdate();
        clearGameRoomStatement.executeUpdate();
        clearMemberStatement.executeUpdate();
        clearParticipationStatement.close();
        clearGameInstanceStatement.close();
        clearGameRoomStatement.close();
        clearMemberStatement.close();
        connection.close();
    }

    @DisplayName("POST /game/rooms/{roomId}/participate - success")
    @Test
    void participate() throws JsonProcessingException {
        this.memberRepository.save(new Member("sample-participant", "nick"));
        this.gameRoomRepository.save(new GameRoom("sample-room", LocalDateTime.now()));
        RequestEntity<ParticipationRequestDto> requestEntity = RequestEntity.post(UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.serverPort)
                        .path("/game/rooms/{roomId}/participate")
                        .buildAndExpand(Map.of("roomId", this.gameRoomCreationResponseDto.getRoomId()))
                        .toUriString())
                .body(this.objectMapper.readValue("""
                        {
                            "newParticipantId": "sample-participant"
                        }
                        """, ParticipationRequestDto.class));

        this.restTemplate.exchange(requestEntity, String.class);
    }
}