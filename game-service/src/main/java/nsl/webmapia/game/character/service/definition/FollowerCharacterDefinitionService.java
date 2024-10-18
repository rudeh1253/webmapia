package nsl.webmapia.game.character.service.definition;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.character.domain.CharacterCode;
import nsl.webmapia.game.character.domain.Faction;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.skill.domain.*;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FollowerCharacterDefinitionService implements CharacterDefinitionService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private SkillUnitProcessor skillProcessorForEnterWolfChat;

    private SkillUnitProcessor skillProcessorForInvestigation;

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

        this.skillProcessorForInvestigation = (act, tar, activatedSkillsToTarget) -> {
            boolean success = tar.isDead()
                    && this.activatedSkillRepository.findByGameInstanceIdAndActivatorId(act.getGameInstance().getGameInstanceId(), act.getAssignmentId()).stream().noneMatch((as) -> as.getSkillType() == SkillType.INVESTIGATE_ALIVE_CHARACTER);
            return new SkillEffect(
                    success ? SkillEffectType.INVESTIGATION_SUCCESS : SkillEffectType.INVESTIGATION_FAIL,
                    act.getAssignmentId(),
                    tar.getAssignmentId(),
                    List.of(act.getAssignmentId()),
                    success ? String.format("%s는 %s입니다.", tar.getMember().getNickname(), tar.getCharacterCode().getTitle())
                            : "실패"
            );
        };
    }

    @Override
    public SkillInfo getSkillOfType(SkillType skillType) {
        return switch (skillType) {
            case ENTER_WOLF_CHAT -> new SkillInfo(skillType, this.skillProcessorForEnterWolfChat);
            case INVESTIGATE_ALIVE_CHARACTER -> new SkillInfo(skillType, this.skillProcessorForInvestigation);
            default -> new SkillInfo();
        };
    }

    @Override
    public Map<SkillType, List<Integer>> getAvailableSkillTypes(int gameInstanceId, Integer characterAssignmentId) {
        List<CharacterAssignment> ca = this.characterAssignmentRepository.findAliveCharacterAssignmentsByGameInstanceId(gameInstanceId);
        Map<SkillType, List<Integer>> availableSkillTypes = new HashMap<>();
        availableSkillTypes.put(
                SkillType.ENTER_WOLF_CHAT,
                ca.stream().map(CharacterAssignment::getAssignmentId).toList()
        );
        insertIfInvestigateAliveCharacterAvailable(
                availableSkillTypes,
                ca,
                gameInstanceId,
                characterAssignmentId
        );
        return Collections.unmodifiableMap(availableSkillTypes);
    }

    private void insertIfInvestigateAliveCharacterAvailable(Map<SkillType, List<Integer>> availableSkillTypes,
                                                            List<CharacterAssignment> ca,
                                                            int gameInstanceId,
                                                            Integer characterAssignmentId) {
        List<ActivatedSkill> activated = this.activatedSkillRepository.findByGameInstanceIdAndActivatorId(gameInstanceId, characterAssignmentId);
        if (activated.stream().anyMatch((as) -> as.getSkillType() == SkillType.INVESTIGATE_ALIVE_CHARACTER)) {
            return;
        }
        availableSkillTypes.put(
                SkillType.INVESTIGATE_ALIVE_CHARACTER,
                ca.stream().map(CharacterAssignment::getAssignmentId).toList()
        );
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
