package nsl.webmapia.game.character.domain.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.CharacterDefinition;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Follower implements CharacterDefinition {
    private int skillCountLeft = 1;

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
