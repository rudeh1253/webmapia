package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

import java.util.Set;

public class Detective extends Character {
    private static final Set<CharacterCode> SKILL_TARGET_CHARACTERS = Set.of(
            CharacterCode.BETRAYER,
            CharacterCode.FOLLOWER
    );

    @Deprecated
    public Detective(String memberId) {
        super(memberId);
    }

    public Detective(String memberId, GameInstance gameInstance) {
        super(memberId, gameInstance);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                SKILL_TARGET_CHARACTERS.contains(tar.getCharacterCode()));
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.DETECTIVE;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
