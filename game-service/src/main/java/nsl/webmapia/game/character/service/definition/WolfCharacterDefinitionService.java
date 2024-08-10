package nsl.webmapia.game.character.service.definition;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WolfCharacterDefinitionService implements CharacterDefinitionService {
    private int leftBeheadCount = 1;

    private final ActivatedSkillRepository activatedSkillRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case BEHEAD -> new SkillInfo(
                    SkillType.BEHEAD,
                    leftBeheadCount-- > 0
                            ? (act, tar, activatedSkillsToTarget) -> !tar.isDead() && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE
                            : (act, tar, activateSkillsToTarget) -> false);
            case KILL -> new SkillInfo(SkillType.KILL, (act, tar, activatedSkillsToTarget) ->
                    !tar.isDead()
                            && tar.getCharacterCode() != CharacterCode.HUMAN_MOUSE
                            && !activatedSkillsToTarget.contains(SkillType.GUARD));
            default -> new SkillInfo();
        };
    }

    @Override
    public List<SkillType> getAvailableSkillTypes(int gameRoomId, String memberId) {
        return this.activatedSkillRepository.findByGameRoomIdAndActivatorId(gameRoomId, memberId)
                .stream()
                .map(ActivatedSkill::getSkillType)
                .toList()
                .contains(SkillType.BEHEAD)
                ? List.of(SkillType.KILL)
                : List.of(SkillType.KILL, SkillType.BEHEAD);
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
