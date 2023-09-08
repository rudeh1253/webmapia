package com.nsl.webmapia.skill.dto;

import com.nsl.webmapia.skill.domain.SkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SkillActivationRequestDTO {
    private Long gameId;
    private Long activatorId;
    private Long targetId;
    private SkillType skillType;
}
