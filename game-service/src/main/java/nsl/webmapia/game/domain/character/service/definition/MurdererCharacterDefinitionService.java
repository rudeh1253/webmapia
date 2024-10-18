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

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MurdererCharacterDefinitionService implements CharacterDefinitionService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;

    private SkillUnitProcessor skillProcessor;

    @PostConstruct
    public void init() {
        this.skillProcessor = (act, tar, activatedSkillToTarget) -> {
            boolean success = isMurderAvailable(act.getGameInstance().getGameInstanceId(), tar.getAssignmentId())
                    && !tar.isDead()
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeById(tar.getAssignmentId(), tar.getLife() - 1);
            }
            return new SkillEffect(
                    success ? SkillEffectType.MURDER_SUCCESS : SkillEffectType.MURDER_FAIL,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    success
                            ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getAssignmentId)
                            .toList()
                            : List.of(act.getAssignmentId()),
                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s가 살인자에 의해 살해당했습니다.", tar.getMember().getNickname())
                            : String.format("%s를 살해하는 데 실패했습니다.", tar.getMember().getNickname())
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        if (isMurderAvailable(gameInstanceId, characterAssignmentId)) {
            List<Integer> targets = this.characterAssignmentRepository.findByGameInstanceId(gameInstanceId)
                    .stream()
                    .filter((ca) -> !ca.isDead())
                    .map(CharacterAssignment::getAssignmentId)
                    .toList();
            return Map.of(SkillType.MURDER, targets);
        } else {
            return Map.of();
        }
    }

    private boolean isMurderAvailable(int gameInstanceId, Integer characterAssignmentId) {
        return this.activatedSkillRepository.findByGameInstanceIdAndActivatorId(gameInstanceId, characterAssignmentId)
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
