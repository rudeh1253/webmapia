package com.nsl.webmapia.gameoperation.domain;

import com.nsl.webmapia.character.Character;
import com.nsl.webmapia.skill.domain.SkillEffect;
import com.nsl.webmapia.skill.domain.SkillManager;
import com.nsl.webmapia.skill.domain.SkillType;
import com.nsl.webmapia.user.repository.UserRepository;
import com.nsl.webmapia.character.CharacterCode;
import com.nsl.webmapia.character.Faction;
import com.nsl.webmapia.user.domain.User;

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

    void onGameStart();

    void setGameName(String name);

    String getGameName();

    void setHost(Long hostId);

    User getHost();

    boolean hasGameStarted();

    void clearGame();

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
     * This method defines process after the phase.
     * If a faction achieves victory at the end of the phase, then it will return the enum of Faction
     * who won, otherwise null.
     */
    Faction postPhase();

    GamePhase currentPhase();

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
     * Add user in repository
     */
    void addUser(Long userId, String userName);

    /**
     * Return all users belongs to the game.
     * @return list of users belonging to the game.
     */
    List<User> getAllUsers();

    Optional<User> getOneUser(Long userId);

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
     * Since the timelapse is processed at the front-end, the server just receives the signal that the time of
     * each phase hits the end. Each client connected to this game sends the end signal, and all users in this game
     * completes to send signals, the game phase steps forward.
     * @param userId of who sends the end signal.
     * @return true if this game receives signals from all users participating in this game, otherwise false.
     */
    boolean endPhase(Long userId);

    /**
     * After the night, process activated skills.
     * @return a list of notification body.
     */
    List<SkillEffect> processSkills();

    boolean endGame(Long userId);
}
