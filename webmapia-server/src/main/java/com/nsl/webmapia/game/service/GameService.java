package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.notification.NotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

import java.util.List;
import java.util.Map;

public interface GameService {

    /**
     * Generate characters and allocate each of the characters to each user.
     * Each user instance stored in repository is mutated in this method.
     * @param characterDistribution the number of each of character to generate.
     * @return information to be sent to each user saying that which character the user is allocated.
     */
    List<NotificationBody<Character>> generateCharacters(Long gameId,
                                                         Map<CharacterCode, Integer> characterDistribution);

    /**
     * The phase of game steps forward.
     */
    void stepForward(Long gameId);

    /**
     * Accept vote.
     * @param voterId id of voter
     * @param subjectId id of subject
     */
    void acceptVote(Long gameId,
                    Long voterId, Long subjectId);

    /**
     * Given votes, determine which user has gotten the most votes such that he will be executed.
     * @return NotificationBody object containing the result of the vote.
     */
    NotificationBody<User> processVotes(Long gameId);

    /**
     * Add user in repository. The id is generated randomly.
     * The value of id is greater than 0.
     * @return id generated.
     */
    void addUser(Long gameId, Long userId);

    /**
     * Remove user from the repository.
     * @param userId of user to remove.
     * @return User object which is removed, if it doesn't exist, return null.
     */
    NotificationBody<User> removeUser(Long gameId, Long userId);

    /**
     * Return all users belongs to the game of gameId.
     * @return list of users belonging to the given game.
     */
    List<User> getAllUsers(Long gameId);

    /**
     * Process activation of a skill.
     * @param activatorId of user which activated skill.
     * @param targetId of user which is the target of the skill.
     * @param skillType of skill activated.
     */
    void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType);

    /**
     * After the night, process activated skills.
     * @return a list of notification body.
     */
    List<NotificationBody<SkillEffect>> processSkills(Long gameId);

    /**
     * Create a new game.
     * @return gameId.
     */
    Long createNewGame();

    List<GameManager> getAllGames();

    public GameManager getGame(Long gameId);
}
