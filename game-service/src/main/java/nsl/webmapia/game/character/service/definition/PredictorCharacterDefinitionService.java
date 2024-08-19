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
            Integer roomId = act.getGameInstance().getGameRoom().getRoomId();
            if (AVAILABLE_KILL.contains(targetCharacter)) {
                this.characterAssignmentRepository.updateLifeByGameRoomIdAndMemberId(roomId, tar.getMemberId(), 0);
                return new SkillEffect(
                        SkillEffectType.KILL_SUCCESS,
                        act.getMemberId(),
                        tar.getMemberId(),
                        this.characterAssignmentRepository.findByGameRoomId(roomId)
                                .stream()
                                .map(CharacterAssignment::getMemberId)
                                .toList(),
                        String.format("%s가 예언자에 의해 사망했습니다.", tar.getMemberId())
                );
            }
            return new SkillEffect(
                    SkillEffectType.INVESTIGATION_SUCCESS,
                    act.getMemberId(),
                    tar.getMemberId(),
                    List.of(act.getMemberId()),
                    String.format("%s는 %s입니다.",
                            tar.getMemberId(),
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
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        return Map.of(
                SkillType.INVESTIGATE_ALIVE_CHARACTER,
                this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameRoomId(gameRoomId)
                        .stream()
                        .map(CharacterAssignment::getMemberId)
                        .toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return null;
    }

    @Override
    public Faction getFaction() {
        return null;
    }
}
