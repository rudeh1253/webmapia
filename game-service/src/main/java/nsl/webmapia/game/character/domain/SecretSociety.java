package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class SecretSociety extends Character {

    public SecretSociety(String memberId) {
        super(memberId);
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo();
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.SECRET_SOCIETY;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
