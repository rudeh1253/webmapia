package com.nsl.webmapia.user.service;

import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.user.domain.User;
import com.nsl.webmapia.user.repository.UserIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final GameRepository gameRepository;
    private UserIdRepository userIdRepository;

    public UserServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public UserServiceImpl(GameRepository gameRepository, UserIdRepository userIdRepository) {
        this.gameRepository = gameRepository;
        this.userIdRepository = userIdRepository;
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

    @Override
    public Long generateId() {
        Random random = new Random();
        Long id = Math.abs(random.nextLong()) % 1000000;
        if (userIdRepository.exist(id)) {
            return generateId();
        }
        userIdRepository.addId(id);
        return id;
    }

    @Override
    public void removeId(Long id) {
        userIdRepository.remove(id);
    }
}
