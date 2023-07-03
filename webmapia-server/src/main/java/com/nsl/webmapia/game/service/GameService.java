package com.nsl.webmapia.game.service;

import com.nsl.webmapia.game.domain.Vote;
import com.nsl.webmapia.game.domain.character.Character;
import com.nsl.webmapia.game.domain.notification.PrivateNotificationBody;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;

import java.util.List;

public interface GameService {

    /**
     * This method is called when a game is start.
     */
    void onStart();

    /**
     * Each user get to have a character. Each user instance is mutated in this method.
     * @return information to be sent to each user saying that which character the user is allocated.
     */
    List<PrivateNotificationBody<Character>> allocateCharacterToEachUser();

    /**
     * The phase of game steps forward.
     */
    void stepForward();

    /**
     * Accept vote.
     * @param vote Vote object
     */
    void acceptVote(Vote vote);

    /**
     * Add user in repository. The id is generated randomly.
     * The value of id is greater than 0.
     * @return id generated.
     */
    Long addUser();

    /**
     * Remove user from the repository.
     * @param userId of user to remove.
     * @return user id of user removed if the user exists, otherwise -1.
     */
    Long removeUser(Long userId);

    /**
     * Process activation of a skill.
     * @param activatorId of user which activated skill.
     * @param targetId of user which is the target of the skill.
     * @param skillType of skill activated.
     */
    void activateSkill(Long activatorId, Long targetId, SkillType skillType);

    /**
     * After the night, process activated skills.
     * @return a list of skill effects.
     */
    List<SkillEffect> processSkills();
}
