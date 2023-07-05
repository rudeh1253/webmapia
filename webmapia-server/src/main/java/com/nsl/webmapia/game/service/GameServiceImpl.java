package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.notification.NotificationType;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.GameRepository;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<NotificationBody<Character>> generateCharacters(Long gameId,
                                                                Map<CharacterCode, Integer> characterDistribution) {
        GameManager gameManager = findGameManager(gameId);
        List<User> users = gameManager.generateCharacters(characterDistribution);
        List<NotificationBody<Character>> notificationBodies = new ArrayList<>();
        for (User user : users) {
            notificationBodies.add(NotificationBody.<Character>builder()
                    .notificationType(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED)
                    .receiver(user)
                    .data(user.getCharacter())
                    .gameId(gameManager.getGameId())
                    .build());
        }
        return notificationBodies;
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
    public NotificationBody<User> processVotes(Long gameId) {
        GameManager game = findGameManager(gameId);
        User mostUser = game.processVotes();
        return mostUser == null
                ? NotificationBody.<User>builder()
                        .gameId(gameId)
                        .notificationType(NotificationType.INVALID_VOTE)
                        .receiver(null)
                        .data(null)
                        .build()
                : NotificationBody.<User>builder()
                        .gameId(gameId)
                        .notificationType(NotificationType.EXECUTE_BY_VOTE)
                        .receiver(null)
                        .data(mostUser)
                        .build();
    }

    @Override
    public void addUser(Long gameId, Long userId) {
        GameManager gameManager = findGameManager(gameId);
        gameManager.addUser(userId);
    }

    @Override
    public NotificationBody<User> removeUser(Long gameId, Long userId) {
        GameManager game = findGameManager(gameId);
        return NotificationBody.<User>builder()
                .gameId(gameId)
                .receiver(null)
                .notificationType(NotificationType.USER_REMOVED)
                .data(game.removeUser(userId).orElse(null))
                .build();
    }

    @Override
    public void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType) {
        GameManager game = findGameManager(gameId);
        game.activateSkill(activatorId, targetId, skillType);
    }

    @Override
    public List<NotificationBody<SkillEffect>> processSkills(Long gameId) {
        GameManager game = findGameManager(gameId);
        List<SkillEffect> skillEffects = game.processSkills();
        List<NotificationBody<SkillEffect>> notificationBodies = new ArrayList<>();
        skillEffects.forEach(se -> {
            if (se.getReceiverUser() == null) {
                notificationBodies.add(NotificationBody.<SkillEffect>builder()
                        .receiver(null)
                        .notificationType(NotificationType.SKILL_PUBLIC)
                        .data(se)
                        .build());
            } else {
                notificationBodies.add(NotificationBody.<SkillEffect>builder()
                        .receiver(se.getReceiverUser())
                        .notificationType(NotificationType.SKILL_PRIVATE)
                        .data(se)
                        .build());
            }
        });
        return notificationBodies;
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
}
