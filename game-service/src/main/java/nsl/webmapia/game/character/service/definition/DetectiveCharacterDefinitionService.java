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
public class DetectiveCharacterDefinitionService implements CharacterDefinitionService {
    private static final Set<CharacterCode> SKILL_TARGET_CHARACTERS = Set.of(
            CharacterCode.BETRAYER,
            CharacterCode.FOLLOWER
    );

    private final CharacterAssignmentRepository characterAssignmentRepository;


    private SkillUnitProcessor skillProcessor;

    @PostConstruct
    public void init() {
        this.skillProcessor = (act, tar, activatedSkillsToTarget) -> {
            boolean success = SKILL_TARGET_CHARACTERS.contains(tar.getCharacterCode());
            return new SkillEffect(
                    success ? SkillEffectType.INVESTIGATION_SUCCESS : SkillEffectType.INVESTIGATION_FAIL,
                    act.getMemberId(),
                    tar.getMemberId(),
                    List.of(act.getMemberId()),
                    // TODO: Replace hard code with MessageSource
                    success ? String.format("%s는 %s입니다.", tar.getMemberId(), tar.getCharacterCode().getTitle())
                            : "실패"
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return new SkillInfo(skillType, this.skillProcessor);
    }

    @Override
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        List<CharacterAssignment> characterAssignments = this.characterAssignmentRepository.findByGameRoomId(gameRoomId);
        return Map.of(
                SkillType.INVESTIGATE_ALIVE_CHARACTER,
                characterAssignments.stream().filter((ca) -> !ca.isDead()).map(CharacterAssignment::getMemberId).toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.DETECTIVE;
    }

    @Override
    public Faction getFaction() {
        return Faction.HUMAN;
    }
}
