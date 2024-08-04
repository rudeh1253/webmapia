package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Templar extends Character {

    @Deprecated
    public Templar(String memberId) {
        super(memberId);
    }

    public Templar(String memberId, GameInstance gameInstance) {
        super(memberId, gameInstance);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
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
