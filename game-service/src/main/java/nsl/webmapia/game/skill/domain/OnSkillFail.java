package nsl.webmapia.game.skill.domain;

import nsl.webmapia.game.member.domain.Member;

@Deprecated
@FunctionalInterface
public interface OnSkillFail {

    void onSkillFail(Member source, Member target, SkillType skillType);
}
