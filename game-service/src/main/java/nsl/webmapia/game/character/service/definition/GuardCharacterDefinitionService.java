package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GuardCharacterDefinitionService implements CharacterDefinitionService {
    private final SkillUnitProcessor skillProcessor = (act, tar, activatedSkillsToTarget) -> {
        if (!activatedSkillsToTarget.contains(SkillType.BEHEAD)
                && !activatedSkillsToTarget.contains(SkillType.MURDER)
                && activatedSkillsToTarget.contains(SkillType.KILL)) {
            return new SkillEffect(
                    SkillEffectType.GUARD_SUCCESS,
                    act.getMemberId(),
                    tar.getMemberId(),
                    List.of(act.getMemberId()),
                    // TODO: Replace hard code with MessageSource
                    String.format("%s를 살리는 데 성공했습니다.", tar.getMemberId())
            );
        } else {
            return new SkillEffect(
                    SkillEffectType.GUARD_FAIL,
                    act.getMemberId(),
                    tar.getMemberId(),
                    List.of(act.getMemberId()),
                    // TODO: Replace hard code with MessageSource
                    "실패했습니다."
            );
        }
    };

    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
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
