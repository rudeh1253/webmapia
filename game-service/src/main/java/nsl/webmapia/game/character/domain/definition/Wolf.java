package nsl.webmapia.game.character.domain.definition;

import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.CharacterDefinition;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

@Component
public class Wolf implements CharacterDefinition {
    private int leftBeheadCount = 1;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case BEHEAD -> new SkillInfo(
                    SkillType.BEHEAD,
                    leftBeheadCount-- > 0
                            ? (act, tar, activatedSkillsToTarget) -> !tar.isDead() && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE
                            : (act, tar, activateSkillsToTarget) -> false);
            case KILL -> new SkillInfo(SkillType.KILL, (act, tar, activatedSkillsToTarget) ->
                    !tar.isDead()
                            && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE
                            && !activatedSkillsToTarget.contains(SkillType.GUARD));
            default -> new SkillInfo();
        };
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.WOLF;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}
