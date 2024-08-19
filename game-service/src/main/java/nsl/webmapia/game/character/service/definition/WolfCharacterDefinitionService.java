package nsl.webmapia.game.character.service.definition;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.*;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
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
            boolean success = isBeheadAvailable(act.getGameInstance().getGameInstanceId(), act.getMemberId())
                    && !tar.isDead()
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeByGameInstanceIdAndMemberId(
                        act.getGameInstance().getGameInstanceId(),
                        tar.getMemberId(),
                        tar.getLife() - 1
                );
            }
            return new SkillEffect(
                    success ? SkillEffectType.BEHEAD_SUCCESS : SkillEffectType.BEHEAD_FAIL,
                    act.getMemberId(),
                    tar.getMemberId(),
                    success ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getMemberId)
                            .toList()
                            : List.of(act.getMemberId()),
                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s가 늑대에 의해 참살당했습니다.", tar.getMemberId())
                            : String.format("%s를 참살하는 데 실패했습니다.", tar.getMemberId())
            );
        };

        this.skillProcessorForKill = (act, tar, activatedSkillsToTarget) -> {
            boolean success = !tar.isDead()
                    && !activatedSkillsToTarget.contains(SkillType.GUARD)
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeByGameInstanceIdAndMemberId(
                        act.getGameInstance().getGameInstanceId(),
                        tar.getMemberId(),
                        tar.getLife() - 1
                );
            }
            return new SkillEffect(
                    success ? SkillEffectType.KILL_SUCCESS : SkillEffectType.KILL_FAIL,
                    act.getMemberId(),
                    tar.getMemberId(),
                    success ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getMemberId)
                            .toList()
                            : List.of(act.getMemberId()),
                    success ? String.format("%s가 늑대에 의해 죽었습니다.", tar.getMemberId())
                            : String.format("%s를 죽이는 데 실패했습니다.", tar.getMemberId())
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
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameInstanceId, String memberId) {
        List<String> aliveMemberIds = this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId)
                .stream()
                .map(CharacterAssignment::getMemberId)
                .toList();
        Map<SkillType, List<String>> result = new HashMap<>();
        result.put(SkillType.KILL, aliveMemberIds);
        if (isBeheadAvailable(gameInstanceId, memberId)) {
            result.put(SkillType.BEHEAD, aliveMemberIds);
        }
        return Collections.unmodifiableMap(result);
    }

    private boolean isBeheadAvailable(int gameInstanceId, String memberId) {
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
