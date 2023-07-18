package com.nsl.webmapia.gameoperation.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.character.Character;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.user.domain.User;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.dto.response.*;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements  GameService {
    private final GameRepository gameRepository;
    private final Map<CharacterCode, Character> characters;

    @Autowired
    public GameServiceImpl(Characters characters,
                           GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.characters = characters.getCharacters();
    }

    @Override
    public List<CharacterGenerationResponseDTO> generateCharacters(Long gameId,
                                                                   Map<CharacterCode, Integer> characterDistribution) {
        GameManager gameManager = findGameManager(gameId);
        final List<User> users = gameManager.generateCharacters(characterDistribution);
        final List<CharacterGenerationResponseDTO> dtoList = new ArrayList<>();

        users.forEach(user ->
                dtoList.add(CharacterGenerationResponseDTO.from(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED,
                        user.getID(),
                        user.getCharacter().getCharacterCode(),
                        gameId)));
        return dtoList;
    }

    @Override
    public void postPhase(Long gameId) {
        GameManager game = findGameManager(gameId);
        game.postPhase();
    }

    @Override
    public void acceptVote(Long gameId, Long voterId, Long subjectId) {
        GameManager game = findGameManager(gameId);
        game.acceptVote(voterId, subjectId);
    }

    @Override
    public VoteResultResponseDTO processVotes(Long gameId) {
        GameManager game = findGameManager(gameId);
        User mostUser = game.processVotes();
        return mostUser == null
                ? VoteResultResponseDTO.from(NotificationType.INVALID_VOTE, gameId, null)
                : VoteResultResponseDTO.from(NotificationType.EXECUTE_BY_VOTE, gameId, mostUser.getID());
    }

    @Override
    public PhaseEndNotificationDTO phaseEnd(Long gameId, Long userId) {
        GameManager game = findGameManager(gameId);
        boolean isEnd = game.endPhase(userId);
        return PhaseEndNotificationDTO.from(gameId, isEnd);
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }
}
