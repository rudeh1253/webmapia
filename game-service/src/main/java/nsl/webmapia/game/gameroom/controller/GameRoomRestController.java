package nsl.webmapia.game.gameroom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nsl.webmapia.game.common.dto.PageWrapper;
import nsl.webmapia.game.common.rest.BaseResponse;
import nsl.webmapia.game.gameroom.dto.GameRoomDto;
import nsl.webmapia.game.gameroom.dto.request.GameRoomCreationRequestDto;
import nsl.webmapia.game.gameroom.dto.request.GameRoomRequestDto;
import nsl.webmapia.game.gameroom.dto.response.GameRoomCreationResponseDto;
import nsl.webmapia.game.gameroom.service.GameRoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/game/rooms")
public class GameRoomRestController {
    private final GameRoomService gameRoomService;
    private final String baseUrl;

    public GameRoomRestController(GameRoomService gameRoomService, @Value("${custom.server.base-url}") String baseUrl) {
        this.gameRoomService = gameRoomService;
        this.baseUrl = baseUrl;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Room creation",
            description = "API for creating a game room"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Succeed to create a room.",
                    headers = @Header(name = "Location", description = "It contains the url of the room")
            )
    })
    public ResponseEntity<BaseResponse<GameRoomCreationResponseDto>> createRoom(@RequestBody GameRoomCreationRequestDto dto)
            throws URISyntaxException {
        GameRoomCreationResponseDto result = this.gameRoomService.createRoom(dto.getRoomName(), dto.getCreatorId());
        return ResponseEntity.created(new URI(this.baseUrl + "/room/" + result.getRoomId()))
                .body(BaseResponse.created("New room created", result));
    }

    @GetMapping("/{roomId}")
    @Operation(
            summary = "Get a room",
            description = "Get a room info of roomId"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "This can occur if roomId is not number format"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<BaseResponse<GameRoomDto>> getGameRoom(@PathVariable("roomId") int roomId) {
        return ResponseEntity.ok(BaseResponse.ok("Get room of id: " + roomId, this.gameRoomService.getGameRoom(roomId)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageWrapper<GameRoomDto>>> getGameRooms(
            @ModelAttribute GameRoomRequestDto requestDto
    ) {
        return ResponseEntity.ok(BaseResponse.ok("Get a list of game room", this.gameRoomService.getGameRooms(requestDto)));
    }
}
