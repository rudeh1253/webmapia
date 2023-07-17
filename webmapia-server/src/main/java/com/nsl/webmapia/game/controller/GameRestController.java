package com.nsl.webmapia.game.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.game.dto.request.GameCreationRequestDTO;
import com.nsl.webmapia.game.dto.response.GameInfoDTO;
import com.nsl.webmapia.game.dto.response.UserResponseDTO;
import com.nsl.webmapia.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*")
public class GameRestController {
    private GameService gameService;

    @Autowired
    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/create-room")
    public ResponseEntity<CommonResponse> createRoom(@RequestBody GameCreationRequestDTO request) {
        Long newGameId = gameService.createNewGame();
        GameInfoDTO gameInfo = gameService.getGame(newGameId);
        return CommonResponse.ok(gameInfo, LocalDateTime.now());
    }

    @GetMapping("/game/{gameId}/user")
    public ResponseEntity<CommonResponse> getUsers(@PathVariable("gameId") Long gameId) {
        List<UserResponseDTO> users = gameService.getAllUsers(gameId);
        return CommonResponse.ok(users, LocalDateTime.now());
    }
}
