package nsl.webmapia.game.skill.domain;

import nsl.webmapia.game.member.domain.Member;

@Deprecated
@FunctionalInterface
public interface OnSkillSucceed {

    void onSkillSucceed(Member source, Member target, SkillType skillType);
}
