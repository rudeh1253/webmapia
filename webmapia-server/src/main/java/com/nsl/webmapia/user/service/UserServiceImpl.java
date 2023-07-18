package com.nsl.webmapia.user.service;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.gameoperation.dto.response.UserResponseDTO;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final GameRepository gameRepository;

    @Autowired
    public UserServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void addUser(Long roomId, Long userId) {
        GameManager gameManager = findGameManager(roomId);
        gameManager.addUser(userId);
    }

    @Override
    public void addUser(Long roomId, Long userId, String userName) {
        GameManager gameManager = findGameManager(roomId);
        gameManager.addUser(userId, userName);
    }

    @Override
    public List<UserResponseDTO> getAllUsers(Long roomId) {
        return gameRepository
                .findById(roomId)
                .orElseThrow()
                .getAllUsers()
                .stream()
                .map(user -> UserResponseDTO.from(NotificationType.QUERY_USER, roomId, user))
                .toList();
    }

    @Override
    public UserResponseDTO getUser(Long roomId, Long userId) {
        User user = gameRepository.findById(roomId)
                .orElseThrow()
                .getOneUser(userId)
                .orElseThrow();
        return UserResponseDTO.from(NotificationType.QUERY_USER, roomId, user);
    }

    @Override
    public UserResponseDTO removeUser(Long roomId, Long userId) {
        GameManager game = findGameManager(roomId);
        User userToRemove = game.removeUser(userId).orElseThrow();
        return UserResponseDTO.from(NotificationType.USER_REMOVED, roomId, userToRemove);
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }
}
