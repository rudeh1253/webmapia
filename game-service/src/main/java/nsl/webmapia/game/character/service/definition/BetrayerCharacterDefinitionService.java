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
public class BetrayerCharacterDefinitionService implements CharacterDefinitionService {
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private SkillUnitProcessor skillProcessorForEnterWolfChat;
    private SkillUnitProcessor skillProcessorForInvestigateDeadCharacter;

    @PostConstruct
    public void init() {
        this.skillProcessorForEnterWolfChat = (act, tar, activatedSkillsToTarget) -> new SkillEffect(
                tar.getCharacterCode() == CharacterCode.WOLF ? SkillEffectType.ENTER_WOLF_CHAT_SUCCESS : SkillEffectType.ENTER_WOLF_CHAT_FAIL,
                act.getAssignmentId(),
                tar.getAssignmentId(),
                List.of(act.getAssignmentId()),
                // TODO: Replace hard code with MessageSource
                String.format("%s는 늑대입니다.", tar.getMember().getNickname())
        );

        this.skillProcessorForInvestigateDeadCharacter = (act, tar, activatedSkillsToTarget) -> new SkillEffect(
                tar.isDead() ? SkillEffectType.INVESTIGATION_SUCCESS : SkillEffectType.INVESTIGATION_FAIL,
                act.getAssignmentId(),
                tar.getAssignmentId(),
                List.of(act.getAssignmentId()),
                // TODO: Replace hard code with MessageSource
                String.format("%s는 %s입니다.", tar.getMember().getNickname(), tar.getCharacterCode().getTitle())
        );
    }

    /**
     * Activate one of skill of type, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER.
     * If skillType passed as a parameter is out of the two allowed SkillType, it will throw
     * CharacterNotSupportSkillTypeException as a RuntimeException.
     *
     * @param skillType type of skill to use, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER
     *                  SkillType.ENTER_WOLF_CHAR: Check whether the target user is the wolf and if the target is the
     *                  wolf, enter the wolf chat.
     *                  SkillType.INVESTIGATE_DEAD_CHARACTER: Check the character of the dead user.
     * @return ActivatedSkillInfo Condition to succeed: the skill target has CharacterCode.WOLF.
     */
    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, this.skillProcessorForEnterWolfChat);
            case INVESTIGATE_DEAD_CHARACTER -> new SkillInfo(skillType, this.skillProcessorForInvestigateDeadCharacter);
            default -> new SkillInfo();
        };
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        List<CharacterAssignment> characterAssignments =
                this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId);
        return Map.of(
                SkillType.ENTER_WOLF_CHAT,
                characterAssignments.stream().map(CharacterAssignment::getAssignmentId).toList(),
                SkillType.INVESTIGATE_DEAD_CHARACTER,
                characterAssignments.stream().filter(CharacterAssignment::isDead).map(CharacterAssignment::getAssignmentId).toList()
        );
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.BETRAYER;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}
