package com.nsl.webmapia.room.service;

import com.nsl.webmapia.room.dto.RoomAvailabilityResponseDTO;
import com.nsl.webmapia.room.dto.RoomInfoResponseDTO;

import java.util.List;

public interface RoomService {

    /**
     * Return all games registered.
     * @return list containing all games registered.
     */
    List<RoomInfoResponseDTO> getAllRoomInfo();

    /**
     * Given id, find and return the game.
     * @param gameId id of game.
     * @return game instance. Given id, if there doesn't exist such game, it will return null.
     */
    RoomInfoResponseDTO getRoomInfo(Long gameId);

    /**
     * Create a new game. The id of the game is generated at random. No duplicate of game id is allowed.
     * @return gameId.
     */
    Long createNewRoom(String roomName, Long hostId);

    /**
     * Create a new game. The id of the game is generated at random. No duplicate of game id is allowed.
     * @return gameId.
     */
    Long createNewRoom(String roomName, Long hostId, String hostName);

    RoomAvailabilityResponseDTO isRoomAvailable(Long gameId);
}
