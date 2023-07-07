package com.nsl.webmapia.game.domain;

import com.nsl.webmapia.game.domain.character.*;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillManager;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.repository.UserRepository;

import java.util.*;

public interface GameManager {
    Set<Long> gameIdSet = Collections.synchronizedSet(new HashSet<>());

    static GameManager newInstance(Map<CharacterCode, Character> characters,
                                   SkillManager skillManager,
                                   UserRepository userRepository) {
        Random random = new Random();
        Long gameId = random.nextLong(100000L, 999999L);
        if (gameIdSet.contains(gameId)) {
            return newInstance(characters, skillManager, userRepository);
        } else {
            gameIdSet.add(gameId);
            return new GameManagerImpl(gameId, characters, skillManager,
                    userRepository);
        }
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
    List<User> generateCharacters(Map<CharacterCode, Integer> characterDistribution);

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
     * @return user who got the most votes. If two of users got the same amount of votes, then returns null.
     */
    User processVotes();

    /**
     * Add user in repository
     */
    void addUser(Long userId);

    /**
     * Return all users belongs to the game.
     * @return list of users belonging to the game.
     */
    List<User> getAllUsers();

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
    List<SkillEffect> processSkills();
}
