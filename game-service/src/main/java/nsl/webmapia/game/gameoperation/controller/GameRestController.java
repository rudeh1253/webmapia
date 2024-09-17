package nsl.webmapia.game.gameoperation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.common.rest.BaseResponse;
import nsl.webmapia.game.gameoperation.dto.GameInstanceDto;
import nsl.webmapia.game.gameoperation.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game/game-instance")
public class GameRestController {
    private final GameService gameService;

    @GetMapping
    @Operation(summary = "Get game instance by roomId",
            description = "If a game room started to run, there is a GameInstance bound to the GameRoom. Query for such GameInstance")
    @ApiResponses({
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "No alive GameInstance bound to the GameRoom of the given roomId", responseCode = "404")
    })
    public ResponseEntity<BaseResponse<GameInstanceDto>> getAliveGameInstanceByGameRoomId(@RequestParam("gameRoomId") int gameRoomId) {
        return ResponseEntity.ok(BaseResponse.ok(this.gameService.getAliveGameInstanceByRoomId(gameRoomId)));
    }
}
