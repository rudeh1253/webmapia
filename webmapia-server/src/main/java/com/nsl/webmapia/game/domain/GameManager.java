package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public interface GameManager {

    static GameManager newInstance(Map<CharacterCode, Character> characters,
                                   SkillManager skillManager,
                                   UserRepository userRepository) {
        Random random = new Random();
        Long gameId = random.nextLong(10000L, 99999L);
        return new GameManagerImpl(gameId, characters, skillManager,
                userRepository);
    }

    /**
     * @return id of this game.
     */
    Long getGameId();

    /**
     * Generate characters and allocate each of the characters to each user.
     * Each user instance stored in repository is mutated in this method.
     * @param characterDistribution the number of each of character to generate.
     * @return information to be sent to each user saying that which character the user is allocated.
     */
    List<NotificationBody<Character>> generateCharacters(Map<CharacterCode, Integer> characterDistribution);

    /**
     * The phase of game steps forward.
     */
    void stepForward();

    /**
     * Accept vote.
     * @param voterId id of voter
     * @param subjectId id of subject
     */
    void acceptVote(Long voterId, Long subjectId);

    /**
     * Given votes, determine which user has gotten the most votes such that he will be executed.
     * @return NotificationBody object containing the result of the vote.
     */
    NotificationBody<User> processVotes();

    /**
     * Add user in repository. The id is generated randomly.
     * The value of id is greater than 0.
     * @return id generated.
     */
    Long addUser();

    /**
     * Remove user from the repository.
     * @param userId of user to remove.
     * @return Optional object containing User object which is removed, or not.
     */
    Optional<User> removeUser(Long userId);

    /**
     * Process activation of a skill.
     * @param activatorId of user which activated skill.
     * @param targetId of user which is the target of the skill.
     * @param skillType of skill activated.
     */
    void activateSkill(Long activatorId, Long targetId, SkillType skillType);

    /**
     * After the night, process activated skills.
     * @return a list of notification body.
     */
    List<NotificationBody<SkillEffect>> processSkills();
}
