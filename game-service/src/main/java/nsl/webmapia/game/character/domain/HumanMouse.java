package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class HumanMouse extends Character {

    @Deprecated
    public HumanMouse(String memberId) {
        super(memberId);
    }

    public HumanMouse(String memberId, GameInstance gameInstance) {
        super(memberId, gameInstance);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.HUMAN_MOUSE;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN_MOUSE;
    }
}
