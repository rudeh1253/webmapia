package nsl.webmapia.game.character.service.definition;

import jakarta.annotation.PostConstruct;
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
    private SkillUnitProcessor skillProcessor;

    @PostConstruct
    public void init() {
        this.skillProcessor = (act, tar, activatedSkillsToTarget) -> {
            if (!activatedSkillsToTarget.contains(SkillType.BEHEAD)
                    && !activatedSkillsToTarget.contains(SkillType.MURDER)
                    && activatedSkillsToTarget.contains(SkillType.KILL)) {
                return new SkillEffect(
                        SkillEffectType.GUARD_SUCCESS,
                        act.getAssignmentId(),
                        tar.getAssignmentId(),
                        List.of(act.getAssignmentId()),
                        // TODO: Replace hard code with MessageSource
                        String.format("%s를 살리는 데 성공했습니다.", tar.getMember().getNickname())
                );
            } else {
                return new SkillEffect(
                        SkillEffectType.GUARD_FAIL,
                        act.getAssignmentId(),
                        tar.getAssignmentId(),
                        List.of(act.getAssignmentId()),
                        // TODO: Replace hard code with MessageSource
                        "실패했습니다."
                );
            }
        };
    }

    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        List<CharacterAssignment> ca =
                this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId);
        return Map.of(
                SkillType.GUARD,
                ca.stream().filter((c) -> !c.isDead()).map(CharacterAssignment::getAssignmentId).toList()
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
