package nsl.webmapia.game.skill.domain;

import lombok.*;
import nsl.webmapia.game.character.domain.Character;
import nsl.webmapia.game.member.domain.Member;

import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class ActivatedSkill {
    private final Character activator;
    private final Character target;
    private final SkillInfo skillInfo;

    public boolean isSuccess(Set<SkillType> activatedSkillsToTarget) {
        return this.skillInfo.getSkillCondition()
                .isSuccess(this.activator, this.target, activatedSkillsToTarget);
    }
}
