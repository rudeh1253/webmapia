package nsl.webmapia.game.domain.gameroom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.domain.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.domain.gameroom.service.GameRoomService;
import nsl.webmapia.game.global.rest.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
class TestGameRoomRestController {

    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    GameRoomService gameRoomService;

    @Value("${custom.server.base-url}")
    String baseUrl;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameRoomRestController(this.gameRoomService, this.baseUrl)).build();
    }

    @Test
    void createRoom() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/game/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomName": "sample-room",
                                    "creatorId": "sample-host"
                                }
                                """)
        ).andDo(log()).andDo((result) -> {
            BaseResponse<GameRoomCreationResponseDto> content = this.om.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertThat(content.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(HttpStatus.valueOf(content.getStatusCode()).is2xxSuccessful()).isTrue();
            assertThat(content.getContent().getRoomName()).isEqualTo("sample-room");
            assertThat(content.getContent().getHostMemberId()).isEqualTo("sample-host");
        }).andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    void getGameRoom_success() throws Exception {
        String contentAsString = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/game/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "roomName": "sample-room",
                                    "creatorId": "sample-host"
                                }
                                """)
        ).andReturn().getResponse().getContentAsString();
        BaseResponse<GameRoomCreationResponseDto> creationDto = this.om.readValue(contentAsString, new TypeReference<>() {
        });
        int roomId = creationDto.getContent().getRoomId();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/game/rooms/{roomId}", roomId))
                .andDo(log()).andDo((result) -> {
                    log.info("result:\n{}", result.getResponse().getContentAsString());
                    GameRoomDto content = this.om.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<GameRoomDto>>() {
                    }).getContent();
                    GameRoomCreationResponseDto creationContent = creationDto.getContent();
                    log.info("content={}", content);
                    assertThat(content.getRoomId()).isEqualTo(creationContent.getRoomId());
                    assertThat(content.getRoomName()).isEqualTo(creationContent.getRoomName());
                    assertThat(content.getCreationTime()).isEqualTo(creationContent.getCreationTime());
                    assertThat(content.getParticipants().get(0).getParticipant().getMemberId()).isEqualTo(creationContent.getHostMemberId());
                });
    }

    // TODO: After adding exception handler, test 400 and 404 response
}