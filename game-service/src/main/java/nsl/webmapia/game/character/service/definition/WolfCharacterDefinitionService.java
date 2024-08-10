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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WolfCharacterDefinitionService implements CharacterDefinitionService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case BEHEAD -> new SkillInfo(
                    SkillType.BEHEAD,
                    (act, tar, activatedSkillsToTarget) -> !tar.isDead() && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE);
            case KILL -> new SkillInfo(SkillType.KILL, (act, tar, activatedSkillsToTarget) ->
                    !tar.isDead()
                            && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE
                            && !activatedSkillsToTarget.contains(SkillType.GUARD));
            default -> new SkillInfo();
        };
    }

    @Override
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        List<String> aliveMemberIds = this.characterAssignmentRepository.findByGameRoomId(gameRoomId)
                .stream()
                .filter((ca) -> !ca.isDead())
                .map(CharacterAssignment::getMemberId)
                .toList();
        Map<SkillType, List<String>> result = new HashMap<>();
        result.put(SkillType.KILL, aliveMemberIds);
        if (isBeheadAvailable(gameRoomId, memberId)) {
            result.put(SkillType.BEHEAD, aliveMemberIds);
        }
        return Collections.unmodifiableMap(result);
    }

    private boolean isBeheadAvailable(int gameRoomId, String memberId) {
        return this.activatedSkillRepository.findByGameRoomIdAndActivatorId(gameRoomId, memberId)
                .stream()
                .noneMatch((as) -> as.getSkillType() == SkillType.BEHEAD);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.WOLF;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}
