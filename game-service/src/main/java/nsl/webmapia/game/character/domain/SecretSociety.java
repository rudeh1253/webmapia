package nsl.webmapia.game.character.domain;

import nsl.webmapia.game.gameoperation.domain.GameInstance;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;

public class SecretSociety extends Character {

    @Deprecated
    public SecretSociety(String memberId) {
        super(memberId);
    }

    public SecretSociety(String memberId, GameInstance gameInstance) {
        super(memberId, gameInstance);
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
