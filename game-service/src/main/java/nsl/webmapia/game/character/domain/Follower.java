package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class Follower extends Character {
    private int skillCountLeft = 1;

    public Follower(String memberId) {
        super(memberId);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.getCharacterCode() == CharacterCode.WOLF);
            case INVESTIGATE_ALIVE_CHARACTER ->
                    new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) -> this.skillCountLeft-- > 0);
            default -> new SkillInfo();
        };
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.FOLLOWER;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}
