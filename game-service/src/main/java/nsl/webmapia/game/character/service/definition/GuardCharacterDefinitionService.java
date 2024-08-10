package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillCondition;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GuardCharacterDefinitionService implements CharacterDefinitionService {
    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        SkillCondition condition = (act, tar, activatedSkillsToTarget) ->
                !activatedSkillsToTarget.contains(SkillType.BEHEAD)
                        && !activatedSkillsToTarget.contains(SkillType.MURDER)
                        && activatedSkillsToTarget.contains(SkillType.KILL);
        return new SkillInfo(skillType, condition);
    }

    @Override
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        List<CharacterAssignment> ca = this.characterAssignmentRepository.findByGameRoomId(gameRoomId);
        return Map.of(
                SkillType.GUARD,
                ca.stream().filter((c) -> !c.isDead()).map(CharacterAssignment::getMemberId).toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.GUARD;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
