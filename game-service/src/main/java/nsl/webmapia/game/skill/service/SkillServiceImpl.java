package nsl.webmapia.game.skill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.character.service.definition.WolfCharacterDefinitionService;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.skill.domain.SkillEffect;
import nsl.webmapia.game.skill.domain.SkillInfo;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements SkillService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactory;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final GameInstanceRepository gameInstanceRepository;

    @Override
    public Map<SkillType, List<String>> getAvailableSkills(int gameInstanceId, String memberId) {
        // TODO: IllegalStateException is appropriate here?
        CharacterAssignment characterAssignment =
                this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, memberId)
                        .orElseThrow(IllegalStateException::new);
        log.debug("characterAssignment.memberId={}", characterAssignment.getMemberId());
        log.debug("characterAssignment.characterCode={}", characterAssignment.getCharacterCode());
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactory.getCharacterDefinitionOfCharacterCode(characterAssignment.getCharacterCode());
        log.debug("characterDefinitionService instanceof WolfCharacterDefinitionService={}",
                characterDefinitionService instanceof WolfCharacterDefinitionService);
        return characterDefinitionService.getAvailableSkillTypes(gameInstanceId, memberId);
    }

    @Override
    public void activateSkill(int gameInstanceId, String activatorId, String targetId, SkillType skillType)
            throws IllegalStateException, NoSuchElementException {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(NoSuchElementException::new);
        if (isGameInstanceNotNight(gameInstance)) {
            throw new IllegalStateException();
        }
        ActivatedSkill activatedSkill = new ActivatedSkill();
        activatedSkill.setSkillType(skillType);
        activatedSkill.setRound(gameInstance.getRound());
        activatedSkill.setActivator(this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, activatorId)
                .orElseThrow(NoSuchElementException::new));
        activatedSkill.setTarget(this.characterAssignmentRepository.findByGameInstanceIdAndMemberId(gameInstanceId, targetId)
                .orElseThrow(NoSuchElementException::new));
        this.activatedSkillRepository.save(activatedSkill);
    }

    private boolean isGameInstanceNotNight(GameInstance gameInstance) {
        return gameInstance.getGamePhase() != GamePhase.NIGHT;
    }

    @Override
    public List<SkillEffect> processSkills(int gameInstanceId) {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(NoSuchElementException::new);
        List<ActivatedSkill> activatedSkillsOnRound =
                this.activatedSkillRepository.findByGameInstanceIdAndRound(gameInstance.getGameInstanceId(), gameInstance.getRound());

        List<SkillEffect> skillEffects = new ArrayList<>();
        Map<String, Set<ActivatedSkill>> targetMap = wrapBasedOnTarget(activatedSkillsOnRound);
        for (String targetId : targetMap.keySet()) {
            Set<ActivatedSkill> activatedSkillsToTarget = targetMap.get(targetId);
            for (ActivatedSkill activatedSkillOnTarget : activatedSkillsToTarget) {
                CharacterDefinitionService characterDefinitionService =
                        this.characterDefinitionFactory.getCharacterDefinitionOfCharacterCode(activatedSkillOnTarget.getActivator().getCharacterCode());
                SkillInfo skillInfo = characterDefinitionService.getSkillOfType(activatedSkillOnTarget.getSkillType());
                SkillEffect skillEffect = skillInfo.getSkillUnitProcessor()
                        .process(
                                activatedSkillOnTarget.getActivator(),
                                activatedSkillOnTarget.getTarget(),
                                activatedSkillsToTarget.stream().map(ActivatedSkill::getSkillType).collect(Collectors.toSet())
                        );
                skillEffects.add(skillEffect);
            }
        }
        return skillEffects;
    }

    private Map<String, Set<ActivatedSkill>> wrapBasedOnTarget(List<ActivatedSkill> activatedSkills) {
        Map<String, Set<ActivatedSkill>> activatedSkillMapToTarget = new HashMap<>();
        for (ActivatedSkill as : activatedSkills) {
            if (!activatedSkillMapToTarget.containsKey(as.getTarget().getMemberId())) {
                Set<ActivatedSkill> asSet = new HashSet<>();
                asSet.add(as);
                activatedSkillMapToTarget.put(as.getTarget().getMemberId(), asSet);
            } else {
                activatedSkillMapToTarget.get(as.getTarget().getMemberId()).add(as);
            }
        }
        return activatedSkillMapToTarget;
    }
}
