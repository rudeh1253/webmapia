package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.notification.GameNotificationType;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.dto.*;
import com.nsl.webmapia.game.repository.GameRepository;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
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
    public GameServiceImpl(Wolf wolf,
                           Betrayer betrayer,
                           Citizen citizen,
                           Detective detective,
                           Follower follower,
                           Guard guard,
                           HumanMouse humanMouse,
                           Mediumship mediumship,
                           Murderer murderer,
                           Nobility nobility,
                           Predictor predictor,
                           SecretSociety secretSociety,
                           Soldier soldier,
                           Successor successor,
                           Templar templar,
                           GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        characters = Map.ofEntries(
                Map.entry(CharacterCode.WOLF, wolf),
                Map.entry(CharacterCode.BETRAYER, betrayer),
                Map.entry(CharacterCode.DETECTIVE, detective),
                Map.entry(CharacterCode.FOLLOWER, follower),
                Map.entry(CharacterCode.CITIZEN, citizen),
                Map.entry(CharacterCode.GUARD, guard),
                Map.entry(CharacterCode.HUMAN_MOUSE, humanMouse),
                Map.entry(CharacterCode.MEDIUMSHIP, mediumship),
                Map.entry(CharacterCode.MURDERER, murderer),
                Map.entry(CharacterCode.NOBILITY, nobility),
                Map.entry(CharacterCode.PREDICTOR, predictor),
                Map.entry(CharacterCode.SECRET_SOCIETY, secretSociety),
                Map.entry(CharacterCode.SOLDIER, soldier),
                Map.entry(CharacterCode.SUCCESSOR, successor),
                Map.entry(CharacterCode.TEMPLAR, templar)
        );
    }

    @Override
    public List<CharacterGenerationResponseDTO> generateCharacters(Long gameId,
                                                                   Map<CharacterCode, Integer> characterDistribution) {
        GameManager gameManager = findGameManager(gameId);
        final List<User> users = gameManager.generateCharacters(characterDistribution);
        final List<CharacterGenerationResponseDTO> dtoList = new ArrayList<>();

        users.forEach(user ->
                dtoList.add(CharacterGenerationResponseDTO.from(GameNotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED,
                        user.getID(),
                        user.getCharacter().getCharacterCode(),
                        gameId)));
        return dtoList;
    }

    @Override
    public void stepForward(Long gameId) {

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
                ? VoteResultResponseDTO.from(GameNotificationType.INVALID_VOTE, gameId, null)
                : VoteResultResponseDTO.from(GameNotificationType.EXECUTE_BY_VOTE, gameId, mostUser.getID());
    }

    @Override
    public void addUser(Long gameId, Long userId) {
        GameManager gameManager = findGameManager(gameId);
        gameManager.addUser(userId);
    }

    @Override
    public List<UserResponseDTO> getAllUsers(Long gameId) {
        return gameRepository
                .findById(gameId)
                .orElseThrow()
                .getAllUsers()
                .stream()
                .map(user -> UserResponseDTO.from(GameNotificationType.QUERY_USER, gameId, user))
                .toList();
    }

    @Override
    public UserResponseDTO removeUser(Long gameId, Long userId) {
        GameManager game = findGameManager(gameId);
        User userToRemove = game.removeUser(userId).orElseThrow();
        return UserResponseDTO.from(GameNotificationType.USER_REMOVED, gameId, userToRemove);
    }

    @Override
    public void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType) {
        GameManager game = findGameManager(gameId);
        game.activateSkill(activatorId, targetId, skillType);
    }

    @Override
    public List<SkillResultDTO> processSkills(Long gameId) {
        GameManager game = findGameManager(gameId);
        List<SkillEffect> skillEffects = game.processSkills();
        final List<SkillResultDTO> result = new ArrayList<>();
        skillEffects.forEach(se -> {
            if (se.getReceiverUser() == null) {
                result.add(SkillResultDTO.from(gameId, se, GameNotificationType.SKILL_PUBLIC));
            } else {
                result.add(SkillResultDTO.from(gameId, se, GameNotificationType.SKILL_PRIVATE));
            }
        });
        return result;
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }

    @Override
    public synchronized Long createNewGame() {
        GameManager game = GameManager.newInstance(characters, new SkillManager(), new MemoryUserRepository());
        gameRepository.save(game);
        return game.getGameId();
    }

    @Override
    public List<GameInfoDTO> getAllGames() {
        return gameRepository.findAll().stream()
                .map(GameInfoDTO::from)
                .toList();
    }

    @Override
    public GameInfoDTO getGame(Long gameId) {
        GameManager gameManager = gameRepository.findById(gameId).orElse(null);
        return gameManager != null
                ? GameInfoDTO.from(gameManager)
                : null;
    }
}
