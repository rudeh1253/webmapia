package nsl.webmapia.game.domain.skill.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SkillInfo {
    private final SkillType skillType;
    private final SkillUnitProcessor skillUnitProcessor;

    public SkillInfo() {
        this(SkillType.NONE, (act, tar, activatedSkillsToType) -> new SkillEffect());
    }

    public SkillInfo(SkillType skillType, SkillUnitProcessor skillUnitProcessor) {
        this.skillType = skillType;
        this.skillUnitProcessor = skillUnitProcessor;
    }
}
