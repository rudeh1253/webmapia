package nsl.webmapia.game.domain.character.service.definition;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.domain.character.domain.CharacterCode;
import nsl.webmapia.game.domain.character.domain.Faction;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.skill.domain.*;
import nsl.webmapia.game.domain.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WolfCharacterDefinitionService implements CharacterDefinitionService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;

    private SkillUnitProcessor skillProcessorForBehead;
    private SkillUnitProcessor skillProcessorForKill;

    @PostConstruct
    public void init() {
        this.skillProcessorForBehead = (act, tar, activatedSkillsToTarget) -> {
            boolean success = isBeheadAvailable(act.getGameInstance().getGameInstanceId(), act.getAssignmentId())
                    && !tar.isDead()
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeById(
                        tar.getAssignmentId(),
                        tar.getLife() - 1
                );
            }
            return new SkillEffect(
                    success ? SkillEffectType.BEHEAD_SUCCESS : SkillEffectType.BEHEAD_FAIL,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    success ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getAssignmentId)
                            .toList()
                            : List.of(act.getAssignmentId()),
                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s가 늑대에 의해 참살당했습니다.", tar.getMember().getNickname())
                            : String.format("%s를 참살하는 데 실패했습니다.", tar.getMember().getNickname())
            );
        };

        this.skillProcessorForKill = (act, tar, activatedSkillsToTarget) -> {
            boolean success = !tar.isDead()
                    && !activatedSkillsToTarget.contains(SkillType.GUARD)
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeById(
                        tar.getAssignmentId(),
                        tar.getLife() - 1
                );
            }
            return new SkillEffect(
                    success ? SkillEffectType.KILL_SUCCESS : SkillEffectType.KILL_FAIL,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    success ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getAssignmentId)
                            .toList()
                            : List.of(act.getAssignmentId()),
                    success ? String.format("%s가 늑대에 의해 죽었습니다.", tar.getMember().getNickname())
                            : String.format("%s를 죽이는 데 실패했습니다.", tar.getMember().getNickname())
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case BEHEAD -> new SkillInfo(SkillType.BEHEAD, this.skillProcessorForBehead);
            case KILL -> new SkillInfo(SkillType.KILL, this.skillProcessorForKill);
            default -> new SkillInfo();
        };
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        List<Integer> aliveCharacterAssignmentIds =
                this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId)
                        .stream()
                        .map(CharacterAssignment::getAssignmentId)
                        .toList();
        Map<SkillType, List<Integer>> result = new HashMap<>();
        result.put(SkillType.KILL, aliveCharacterAssignmentIds);
        if (isBeheadAvailable(gameInstanceId, characterAssignmentId)) {
            result.put(SkillType.BEHEAD, aliveCharacterAssignmentIds);
        }
        return Collections.unmodifiableMap(result);
    }

    private boolean isBeheadAvailable(int gameInstanceId, Integer memberId) {
        return this.activatedSkillRepository.findByGameInstanceIdAndActivatorId(gameInstanceId, memberId)
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
