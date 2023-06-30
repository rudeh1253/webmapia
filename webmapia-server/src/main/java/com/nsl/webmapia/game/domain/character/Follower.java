package com.nsl.webmapia.game.domain.character;

import com.nsl.webmapia.common.exception.CharacterNotSupportSkillTypeException;
import com.nsl.webmapia.game.domain.User;
import com.nsl.webmapia.game.domain.skill.SkillEffect;
import com.nsl.webmapia.game.domain.skill.SkillType;
import com.nsl.webmapia.game.service.PublicNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Follower implements Character {
    private static final CharacterCode CHARACTER_CODE = CharacterCode.FOLLOWER;
    private static final Faction FACTION = Faction.WOLF;
    private PublicNotificationService publicNotificationService;

    @Autowired
    public Follower(PublicNotificationService publicNotificationService) {
        this.publicNotificationService = publicNotificationService;
    }

    @Override
    public SkillEffect activateSkill(SkillType skillType) {
        if (skillType != SkillType.INVESTIGATE_ALIVE_CHARACTER) {
            throw new CharacterNotSupportSkillTypeException("Follower doesn't support given skill type: SkillType code "
                    + skillType);
        }
        // TODO: set skill condition
        return null;
    }

    @Override
    public CharacterCode getCharacterCode() {
        return null;
    }

    @Override
    public Faction getFaction() {
        return null;
    }

}
