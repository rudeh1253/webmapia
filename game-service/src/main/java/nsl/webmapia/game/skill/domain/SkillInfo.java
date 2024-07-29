package nsl.webmapia.game.skill.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SkillInfo {
    private final SkillType skillType;
    private final SkillCondition skillCondition;

    public SkillInfo() {
        this(SkillType.NONE, (act, tar, activatedSkillsToType) -> false);
    }

    public SkillInfo(SkillType skillType, SkillCondition skillCondition) {
        this.skillType = skillType;
        this.skillCondition = skillCondition;
    }
}
