package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.GameManager;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.character.CharacterCode;
import com.nsl.webmapia.game.domain.notification.GameNotification;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.dto.CharacterGenerationResponseDTO;
import com.nsl.webmapia.game.dto.UserResponseDTO;
import com.nsl.webmapia.game.dto.VoteResultResponseDTO;

import java.util.List;
import java.util.Map;

public interface GameService {

    /**
     * Generate characters and allocate each of the characters to each user in the game provided.
     * Inner state of each user instance stored in repository is mutated in this method.
     * @param gameId id of game.
     * @param characterDistribution key:value -> Character : # each of character to show up in this game.
     * @return information to be sent to each user saying that which character the user is allocated.
     *         notification type: enum(string), receiver id: number, character code: enum(string), game id: number
     *         will be specified.
     */
    List<CharacterGenerationResponseDTO> generateCharacters(Long gameId,
                                                            Map<CharacterCode, Integer> characterDistribution);

    /**
     * The phase of game steps forward.
     * @param gameId id of game.
     */
    void stepForward(Long gameId);

    /**
     * Accept vote of each user for execution.
     * @param gameId id of game.
     * @param voterId id of voter.
     * @param subjectId id of subject.
     */
    void acceptVote(Long gameId,
                    Long voterId, Long subjectId);

    /**
     * Given votes, determine which user has gotten the most votes such that he is supposed to be executed.
     * After the process of votes, the container of votes is going to get cleared.
     * @param gameId id of game.
     * @return DTO for notification of the result of the vote. If there exists tie such that the vote is invalid,
     *         idOfUserToBeExecuted field is set to null.
     */
    VoteResultResponseDTO processVotes(Long gameId);

    /**
     * Add user in the game.
     */
    void addUser(Long gameId, Long userId);

    /**
     * Remove user from the game. If the user which is supposed to be removed doesn't exist in the game,
     * this will throw an exception.
     * @param gameId id of the game.
     * @param userId of user to remove.
     * @return User object which is removed, if it doesn't exist, throws.
     */
    UserResponseDTO removeUser(Long gameId, Long userId);

    /**
     * Return all users belongs to the game of gameId.
     * @param gameId id of game.
     * @return list of users belonging to the given game.
     */
    List<UserResponseDTO> getAllUsers(Long gameId);

    /**
     * Apply skill. The process of the skill follows the logic of the skill of the character the user
     * who was allocated the character activated. The effect of the skill is going to be processed by calling
     * processSkills(Long) method.
     * @param gameId id of game.
     * @param activatorId of user which activated skill.
     * @param targetId of user which is the target of the skill.
     * @param skillType of skill activated.
     */
    void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType);

    /**
     * After the night, process activated skills.
     * @param gameId id of game.
     * @return a list of notification body.
     */
    List<GameNotification<SkillEffect>> processSkills(Long gameId);

    /**
     * Create a new game. The id of the game is generated at random. No duplicate of game id is allowed.
     * @return gameId.
     */
    Long createNewGame();

    /**
     * Return all games registered.
     * @return list containing all games registered.
     */
    List<GameManager> getAllGames();

    /**
     * Given id, find and return the game.
     * @param gameId id of game.
     * @return game instance. Given id, if there doesn't exist such game, it will return null.
     */
    GameManager getGame(Long gameId);
}
