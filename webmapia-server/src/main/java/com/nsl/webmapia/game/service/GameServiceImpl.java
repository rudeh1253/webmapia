package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.SkillManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.Vote;
import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.NotificationType;
import com.nsl.webmapia.game.domain.notification.PrivateNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class GameServiceImpl implements GameService {
    private Map<CharacterCode, Character> characters;
    private final SkillManager skillManager;
    private final UserRepository userRepository;

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
                           SkillManager skillManager,
                           UserRepository userRepository) {
        characters.put(CharacterCode.WOLF, wolf);
        characters.put(CharacterCode.BETRAYER, betrayer);
        characters.put(CharacterCode.DETECTIVE, detective);
        characters.put(CharacterCode.FOLLOWER, follower);
        characters.put(CharacterCode.CITIZEN, citizen);
        characters.put(CharacterCode.GUARD, guard);
        characters.put(CharacterCode.HUMAN_MOUSE, humanMouse);
        characters.put(CharacterCode.MEDIUMSHIP, mediumship);
        characters.put(CharacterCode.MURDERER, murderer);
        characters.put(CharacterCode.NOBILITY, nobility);
        characters.put(CharacterCode.PREDICTOR, predictor);
        characters.put(CharacterCode.SECRET_SOCIETY, secretSociety);
        characters.put(CharacterCode.SOLDIER, soldier);
        characters.put(CharacterCode.SUCCESSOR, successor);
        characters.put(CharacterCode.TEMPLAR, templar);
        this.skillManager = skillManager;
        this.userRepository = userRepository;
    }

    @Override
    public void onStart() {

    }

    @Override
    public List<PrivateNotificationBody<Character>> allocateCharacterToEachUser(Map<CharacterCode, Integer> characterDistribution) {
        List<User> users = userRepository.findAll();
        List<PrivateNotificationBody<Character>> characterNotifications = new ArrayList<>();
        Collections.shuffle(users);
        int count = 0;
        Set<CharacterCode> codes = characterDistribution.keySet();
        for (CharacterCode code : codes) {
            int num = characterDistribution.get(code);
            for (int i = 0; i < num; i++) {
                User user = users.get(count);
                Character character = characters.get(code);
                user.setCharacter(character);
                characterNotifications.add(PrivateNotificationBody.<Character>builder()
                        .notificationType(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED)
                        .receiver(user)
                        .data(character)
                        .build());
                count++;
            }
        }
        return characterNotifications;
    }

    @Override
    public void stepForward() {

    }

    @Override
    public void acceptVote(Vote vote) {

    }

    @Override
    public Long addUser() {
        return null;
    }

    @Override
    public Long removeUser(Long userId) {
        return null;
    }

    @Override
    public void activateSkill(Long activatorId, Long targetId, SkillType skillType) {

    }

    @Override
    public List<SkillEffect> processSkills() {
        return null;
    }
}
