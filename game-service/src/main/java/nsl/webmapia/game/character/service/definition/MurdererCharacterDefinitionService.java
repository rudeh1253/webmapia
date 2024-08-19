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
            boolean success = isMurderAvailable(act.getGameInstance().getGameRoom().getRoomId(), tar.getMemberId())
                    && !tar.isDead()
                    && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE;
            if (success) {
                this.characterAssignmentRepository.updateLifeByGameRoomIdAndMemberId(act.getGameInstance().getGameRoom().getRoomId(), tar.getMemberId(), tar.getLife() - 1);
            }
            return new SkillEffect(
                    success ? SkillEffectType.MURDER_SUCCESS : SkillEffectType.MURDER_FAIL,
                    act.getMemberId(),
                    tar.getMemberId(),
                    success
                            ? this.characterAssignmentRepository.findByGameInstanceId(act.getGameInstance().getGameInstanceId())
                            .stream()
                            .map(CharacterAssignment::getMemberId)
                            .toList()
                            : List.of(act.getMemberId()),
                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s가 살인자에 의해 살해당했습니다.", tar.getMemberId())
                            : String.format("%s를 살해하는 데 실패했습니다.", tar.getMemberId())
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
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
