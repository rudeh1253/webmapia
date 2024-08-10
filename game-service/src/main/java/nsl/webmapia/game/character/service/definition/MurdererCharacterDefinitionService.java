package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MurdererCharacterDefinitionService implements CharacterDefinitionService {
    private int leftSkillCount = 1;

    private final ActivatedSkillRepository activatedSkillRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                leftSkillCount-- > 0 && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE);
    }

    @Override
    public List<SkillType> getAvailableSkillTypes(int gameRoomId, String memberId) {
        return isMurderAvailable(gameRoomId, memberId)
                ? List.of(SkillType.MURDER)
                : List.of();
    }

    private boolean isMurderAvailable(int gameRoomId, String memberId) {
        return !this.activatedSkillRepository.findByGameRoomIdAndActivatorId(gameRoomId, memberId)
                .stream()
                .map(ActivatedSkill::getSkillType)
                .toList()
                .contains(SkillType.MURDER);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.MURDERER;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
