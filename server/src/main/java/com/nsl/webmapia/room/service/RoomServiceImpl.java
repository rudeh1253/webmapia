package com.nsl.webmapia.room.service;

import com.nsl.webmapia.character.Character;
import com.nsl.webmapia.character.CharacterCode;
import com.nsl.webmapia.character.Characters;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.user.repository.MemoryUserRepository;
import com.nsl.webmapia.room.dto.RoomInfoResponseDTO;
import com.nsl.webmapia.skill.domain.SkillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService{
    private final GameRepository gameRepository;
    private final Map<CharacterCode, Character> characters;

    @Autowired
    public RoomServiceImpl(Characters characters,
                           GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.characters = characters.getCharacters();
    }

    @Override
    public Long createNewRoom(String roomName, Long hostId) {
        GameManager game = GameManager.newInstance(characters, new SkillManager(), new MemoryUserRepository());
        game.setGameName(roomName);
        game.setHost(hostId);
        gameRepository.save(game);
        game.addUser(hostId);
        return game.getGameId();
    }

    @Override
    public Long createNewRoom(String roomName, Long hostId, String hostName) {
        GameManager game = GameManager.newInstance(characters, new SkillManager(), new MemoryUserRepository());
        game.setGameName(roomName);
        game.setHost(hostId);
        gameRepository.save(game);
        game.addUser(hostId, hostName);
        return game.getGameId();
    }

    @Override
    public List<RoomInfoResponseDTO> getAllRoomInfo() {
        return gameRepository.findAll().stream()
                .map(RoomInfoResponseDTO::from)
                .toList();
    }

    @Override
    public RoomInfoResponseDTO getRoomInfo(Long roomId) {
        GameManager gameManager = gameRepository.findById(roomId).orElse(null);
        return gameManager != null
                ? RoomInfoResponseDTO.from(gameManager)
                : null;
    }

    private GameManager findGameManager(Long roomId) {
        return gameRepository.findById(roomId).orElseThrow();
    }
}
