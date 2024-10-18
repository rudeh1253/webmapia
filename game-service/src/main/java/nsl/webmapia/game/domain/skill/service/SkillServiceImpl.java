package nsl.webmapia.game.domain.skill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.domain.character.entity.CharacterAssignment;
import nsl.webmapia.game.domain.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.domain.character.service.CharacterDefinitionService;
import nsl.webmapia.game.domain.gameoperation.domain.GamePhase;
import nsl.webmapia.game.domain.gameoperation.entity.GameInstance;
import nsl.webmapia.game.domain.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.domain.skill.domain.SkillEffect;
import nsl.webmapia.game.domain.skill.domain.SkillInfo;
import nsl.webmapia.game.domain.skill.domain.SkillType;
import nsl.webmapia.game.domain.skill.entity.ActivatedSkill;
import nsl.webmapia.game.domain.skill.repository.ActivatedSkillRepository;
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
    public Map<SkillType, List<Integer>> getAvailableSkills(int gameInstanceId, Integer characterAssignmentId)
            throws IllegalStateException {
        // TODO: IllegalStateException is appropriate here?
        CharacterAssignment characterAssignment =
                this.characterAssignmentRepository.findById(characterAssignmentId)
                        .orElseThrow(NoSuchElementException::new);
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactory.getCharacterDefinitionOfCharacterCode(characterAssignment.getCharacterCode());
        return characterDefinitionService.getAvailableSkillTypes(gameInstanceId, characterAssignmentId);
    }

    @Override
    public void activateSkill(int gameInstanceId, Integer activatorCharacterAssignmentId, Integer targetCharacterAssignmentId, SkillType skillType)
            throws IllegalStateException, NoSuchElementException {
        GameInstance gameInstance = this.gameInstanceRepository.findById(gameInstanceId)
                .orElseThrow(NoSuchElementException::new);
        if (isGameInstanceNotNight(gameInstance)) {
            throw new IllegalStateException();
        }
        ActivatedSkill activatedSkill = new ActivatedSkill();
        activatedSkill.setSkillType(skillType);
        activatedSkill.setRound(gameInstance.getRound());
        activatedSkill.setActivator(this.characterAssignmentRepository.findById(activatorCharacterAssignmentId)
                .orElseThrow(NoSuchElementException::new));
        activatedSkill.setTarget(this.characterAssignmentRepository.findById(targetCharacterAssignmentId)
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
        Map<Integer, Set<ActivatedSkill>> targetMap = wrapBasedOnTarget(activatedSkillsOnRound);
        for (Integer targetId : targetMap.keySet()) {
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

    private Map<Integer, Set<ActivatedSkill>> wrapBasedOnTarget(List<ActivatedSkill> activatedSkills) {
        Map<Integer, Set<ActivatedSkill>> activatedSkillMapToTarget = new HashMap<>();
        for (ActivatedSkill as : activatedSkills) {
            if (!activatedSkillMapToTarget.containsKey(as.getTarget().getAssignmentId())) {
                Set<ActivatedSkill> asSet = new HashSet<>();
                asSet.add(as);
                activatedSkillMapToTarget.put(as.getTarget().getAssignmentId(), asSet);
            } else {
                activatedSkillMapToTarget.get(as.getTarget().getAssignmentId()).add(as);
            }
        }
        return activatedSkillMapToTarget;
    }
}
