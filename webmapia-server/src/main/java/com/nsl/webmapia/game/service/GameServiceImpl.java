package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.GameRepository;
import com.nsl.webmapia.game.repository.MemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Long getGameId() {
        return null;
    }

    @Override
    public List<NotificationBody<Character>> generateCharacters(Map<CharacterCode, Integer> characterDistribution) {
        return null;
    }

    @Override
    public void stepForward() {

    }

    @Override
    public void acceptVote(Long voterId, Long subjectId) {

    }

    @Override
    public NotificationBody<User> processVotes() {
        return null;
    }

    @Override
    public Long addUser() {
        return null;
    }

    @Override
    public Optional<User> removeUser(Long userId) {
        return Optional.empty();
    }

    @Override
    public void activateSkill(Long activatorId, Long targetId, SkillType skillType) {

    }

    @Override
    public List<NotificationBody<SkillEffect>> processSkills() {
        return null;
    }

    @Override
    public synchronized Long createNewGame() {
        GameManager game = GameManager.newInstance(characters, new SkillManager(), new MemoryUserRepository());
        gameRepository.save(game);
        return game.getGameId();
    }
}
