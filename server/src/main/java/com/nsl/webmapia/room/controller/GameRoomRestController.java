package com.nsl.webmapia.room.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.room.dto.RoomAvailabilityResponseDTO;
import com.nsl.webmapia.room.dto.RoomCreationRequestDTO;
import com.nsl.webmapia.room.dto.RoomInfoResponseDTO;
import com.nsl.webmapia.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/app")
@CrossOrigin(originPatterns = "*")
public class GameRoomRestController {
    private RoomService roomService;

    @Autowired
    public GameRoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/game/room")
    public ResponseEntity<CommonResponse> createRoom(@RequestBody RoomCreationRequestDTO request) {
        Long newGameId = roomService.createNewRoom(request.getGameName(), request.getHostId(), request.getHostName());
        RoomInfoResponseDTO gameInfo = roomService.getRoomInfo(newGameId);
        return CommonResponse.ok(gameInfo, LocalDateTime.now());
    }

    @GetMapping("/game/room")
    public ResponseEntity<CommonResponse> getAllRooms() {
        List<RoomInfoResponseDTO> allGames = roomService.getAllRoomInfo();
        return CommonResponse.ok(allGames, LocalDateTime.now());
    }

    @GetMapping("/game/room/{gameId}")
    public ResponseEntity<CommonResponse> getRoomInfo(@PathVariable("gameId") Long gameId) {
        // TODO: Error handling in case of absence of game of such gameId
        RoomInfoResponseDTO gameInfo = roomService.getRoomInfo(gameId);
        return CommonResponse.ok(gameInfo, LocalDateTime.now());
    }

    @GetMapping("/game/room/availability/{gameId}")
    public ResponseEntity<CommonResponse> getRoomAvailability(@PathVariable("gameId") Long gameId) {
        RoomAvailabilityResponseDTO dto = roomService.isRoomAvailable(gameId);
        return CommonResponse.ok(dto, LocalDateTime.now());
    }
}
