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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerCharacterDefinitionService implements CharacterDefinitionService {
    // TODO: skillCountLeft field should be deleted.
    // Skill availability will be recognized by ActivatedSkillRepository.
    private int skillCountLeft = 1;
    private final ActivatedSkillRepository activatedSkillRepository;

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) ->
                    tar.getCharacterCode() == CharacterCode.WOLF);
            case INVESTIGATE_ALIVE_CHARACTER ->
                    new SkillInfo(skillType, (act, tar, activatedSkillsToTarget) -> this.skillCountLeft-- > 0);
            default -> new SkillInfo();
        };
    }

    @Override
    public List<SkillType> getAvailableSkillTypes(int gameRoomId, String memberId) {
        List<SkillType> availableSkillTypes = new ArrayList<>();
        availableSkillTypes.add(SkillType.ENTER_WOLF_CHAT);
        if (isInvestigateAliveCharacterAvailable(gameRoomId, memberId)) {
            availableSkillTypes.add(SkillType.INVESTIGATE_ALIVE_CHARACTER);
        }
        return Collections.unmodifiableList(availableSkillTypes);
    }

    private boolean isInvestigateAliveCharacterAvailable(int gameRoomId, String memberId) {
        return !this.activatedSkillRepository.findByGameRoomIdAndActivatorId(gameRoomId, memberId)
                .stream()
                .map(ActivatedSkill::getSkillType)
                .toList()
                .contains(SkillType.INVESTIGATE_ALIVE_CHARACTER);
    }

    @Override
    public CharacterCode getCharacterCode() {
        return CharacterCode.FOLLOWER;
    }

    @Override
    public Faction getFaction() {
        return Faction.WOLF;
    }
}
