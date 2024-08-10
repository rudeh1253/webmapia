package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BetrayerCharacterDefinitionService implements CharacterDefinitionService {
    private final CharacterAssignmentRepository characterAssignmentRepository;

    /**
     * Activate one of skill of type, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER.
     * If skillType passed as a parameter is out of the two allowed SkillType, it will throw
     * CharacterNotSupportSkillTypeException as a RuntimeException.
     * @param skillType type of skill to use, either SkillType.ENTER_WOLF_CHAT or SkillType.INVESTIGATE_DEAD_CHARACTER
     *                  SkillType.ENTER_WOLF_CHAR: Check whether the target user is the wolf and if the target is the
     *                  wolf, enter the wolf chat.
     *                  SkillType.INVESTIGATE_DEAD_CHARACTER: Check the character of the dead user.
     * @return ActivatedSkillInfo Condition to succeed: the skill target has CharacterCode.WOLF.
     */
    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.getCharacterCode() == CharacterCode.WOLF);
            case INVESTIGATE_DEAD_CHARACTER -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.isDead());
            default -> new SkillInfo();
        };
    }

    @Override
    public Map<SkillType, List<String>> getAvailableSkillTypes(int gameRoomId, String memberId) {
        List<CharacterAssignment> characterAssignments = this.characterAssignmentRepository.findByGameRoomId(gameRoomId);
        return Map.of(
                SkillType.ENTER_WOLF_CHAT,
                characterAssignments.stream().filter((ca) -> !ca.isDead()).map(CharacterAssignment::getMemberId).toList(),
                SkillType.INVESTIGATE_DEAD_CHARACTER,
                characterAssignments.stream().filter(CharacterAssignment::isDead).map(CharacterAssignment::getMemberId).toList()
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
