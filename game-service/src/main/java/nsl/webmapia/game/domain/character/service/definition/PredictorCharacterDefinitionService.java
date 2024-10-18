package nsl.webmapia.game.domain.character.service.definition;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.domain.Faction;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.skill.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PredictorCharacterDefinitionService implements CharacterDefinitionService {
    private static final Set<CharacterCode> AVAILABLE_INVESTIGATION = Set.of(
            CharacterCode.WOLF,
            CharacterCode.GUARD,
            CharacterCode.MEDIUMSHIP
    );
    private static final Set<CharacterCode> AVAILABLE_KILL = Set.of(
            CharacterCode.HUMAN_MOUSE
    );

    private final CharacterAssignmentRepository characterAssignmentRepository;

    private SkillUnitProcessor skillProcessor;

    @PostConstruct
    public void init() {
        this.skillProcessor = (act, tar, activatedSkillsToTarget) -> {
            CharacterCode targetCharacter = tar.getCharacterCode();
            Integer gameInstanceId = act.getGameInstance().getGameInstanceId();
            if (AVAILABLE_KILL.contains(targetCharacter)) {
                this.characterAssignmentRepository.updateLifeById(tar.getAssignmentId(), 0);
                return new SkillEffect(
                        SkillEffectType.KILL_SUCCESS,
                        act.getAssignmentId(),
                        tar.getAssignmentId(),
                        this.characterAssignmentRepository.findByGameInstanceId(gameInstanceId)
                                .stream()
                                .map(CharacterAssignment::getAssignmentId)
                                .toList(),
                        String.format("%s가 예언자에 의해 사망했습니다.", tar.getMember().getNickname())
                );
            }
            return new SkillEffect(
                    SkillEffectType.INVESTIGATION_SUCCESS,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    List.of(act.getAssignmentId()),
                    String.format("%s는 %s입니다.",
                            tar.getAssignmentId(),
                            AVAILABLE_INVESTIGATION.contains(tar.getCharacterCode())
                                    ? tar.getCharacterCode().getTitle()
                                    : CharacterCode.GOOD_PERSON.getTitle())
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(SkillType.INVESTIGATE_ALIVE_CHARACTER, this.skillProcessor);
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        return Map.of(
                SkillType.INVESTIGATE_ALIVE_CHARACTER,
                this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId)
                        .stream()
                        .map(CharacterAssignment::getAssignmentId)
                        .toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.PREDICTOR;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
