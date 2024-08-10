package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MurdererCharacterDefinitionService implements CharacterDefinitionService {
    private int leftSkillCount = 1;

    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                leftSkillCount-- > 0 && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE);
    }

    @Override
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        if (isMurderAvailable(gameRoomId, memberId)) {
            List<String> targets = this.characterAssignmentRepository.findByGameRoomId(gameRoomId)
                    .stream()
                    .filter((ca) -> !ca.isDead())
                    .map(CharacterAssignment::getMemberId)
                    .toList();
            return Map.of(SkillType.MURDER, targets);
        } else {
            return Map.of();
        }
    }

    private boolean isMurderAvailable(int gameRoomId, String memberId) {
        return this.activatedSkillRepository.findByGameRoomIdAndActivatorId(gameRoomId, memberId)
                .stream()
                .noneMatch((as) -> as.getSkillType() == SkillType.MURDER);
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
