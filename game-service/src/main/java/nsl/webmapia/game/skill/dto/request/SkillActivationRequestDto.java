package nsl.webmapia.game.skill.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nsl.webmapia.game.skill.domain.SkillType;

@NoArgsConstructor
@Getter
@ToString
public class SkillActivationRequestDto {
    private Integer activatorCharacterAssignmentId;
    private Integer targetCharacterAssignmentId;
    private SkillType skillType;
}
