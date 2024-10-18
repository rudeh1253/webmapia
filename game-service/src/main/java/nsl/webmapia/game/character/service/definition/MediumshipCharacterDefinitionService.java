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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MediumshipCharacterDefinitionService implements CharacterDefinitionService {
    private final Set<CharacterCode> AVAILABLE_CHARACTER_CODES = Set.of(
            CharacterCode.WOLF,
            CharacterCode.GUARD,
            CharacterCode.PREDICTOR
    );

    private final CharacterAssignmentRepository characterAssignmentRepository;

    private SkillUnitProcessor skillProcessor;

    @PostConstruct
    public void init() {
        this.skillProcessor = (act, tar, activatedSkillsToTarget) -> {
            boolean success = tar.isDead();
            return new SkillEffect(
                    success ? SkillEffectType.INVESTIGATION_SUCCESS : SkillEffectType.INVESTIGATION_FAIL,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    List.of(act.getAssignmentId()),

                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s는 %s입니다.",
                            tar.getAssignmentId(),
                            AVAILABLE_CHARACTER_CODES.contains(tar.getCharacterCode())
                                    ? tar.getCharacterCode().getTitle()
                                    : CharacterCode.GOOD_PERSON.getTitle())
                            : "조사에 실패했습니다."
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        List<CharacterAssignment> deadCharacters =
                this.characterAssignmentRepository.findDeadCharacterAssignmentsByGameInstanceId(gameInstanceId);
        return Map.of(
                SkillType.INVESTIGATE_DEAD_CHARACTER,
                deadCharacters.stream().map(CharacterAssignment::getAssignmentId).toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.MEDIUMSHIP;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
