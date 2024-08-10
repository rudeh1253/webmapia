package nsl.webmapia.game.skill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsl.webmapia.game.character.entity.CharacterAssignment;
import nsl.webmapia.game.character.repository.CharacterAssignmentRepository;
import nsl.webmapia.game.character.service.CharacterDefinitionFactoryService;
import nsl.webmapia.game.character.service.CharacterDefinitionService;
import nsl.webmapia.game.character.service.definition.WolfCharacterDefinitionService;
import nsl.webmapia.game.common.BaseSystemMessageResponseDto;
import nsl.webmapia.game.common.SystemMessageType;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.entity.GameInstance;
import nsl.webmapia.game.gameoperation.repository.GameInstanceRepository;
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.entity.ActivatedSkill;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactory;
    private final CharacterAssignmentRepository characterAssignmentRepository;
    private final GameInstanceRepository gameInstanceRepository;

    @Override
    public BaseSystemMessageResponseDto<Map<SkillType, List<String>>> getAvailableSkills(int gameRoomId, String memberId) {
        // TODO: IllegalStateException is appropriate here?
        CharacterAssignment characterAssignment =
                this.characterAssignmentRepository.findByGameRoomIdAndMemberId(gameRoomId, memberId)
                        .orElseThrow(IllegalStateException::new);
        log.debug("characterAssignment.memberId={}", characterAssignment.getMemberId());
        log.debug("characterAssignment.characterCode={}", characterAssignment.getCharacterCode());
        CharacterDefinitionService characterDefinitionService =
                this.characterDefinitionFactory.getCharacterDefinitionOfCharacterCode(characterAssignment.getCharacterCode());
        log.debug("characterDefinitionService instanceof WolfCharacterDefinitionService={}",
                characterDefinitionService instanceof WolfCharacterDefinitionService);
        return BaseSystemMessageResponseDto.<Map<SkillType, List<String>>>builder()
                .receiverIds(List.of(memberId))
                .systemMessageType(SystemMessageType.AVAILABLE_SKILLS)
                .content(characterDefinitionService.getAvailableSkillTypes(gameRoomId, memberId))
                .build();
    }

    @Override
    public void activateSkill(int gameRoomId, String activatorId, String targetId, SkillType skillType)
            throws IllegalStateException, NoSuchElementException {
        GameInstance gameInstance = this.gameInstanceRepository.findAliveGameInstanceByGameRoomId(gameRoomId)
                .orElseThrow(NoSuchElementException::new);
        if (isGameInstanceNotNight(gameInstance)) {
            throw new IllegalStateException();
        }
        ActivatedSkill activatedSkill = new ActivatedSkill();
        activatedSkill.setSkillType(skillType);
        activatedSkill.setRound(gameInstance.getRound());
        activatedSkill.setActivatorId(activatorId);
        activatedSkill.setTargetId(targetId);
        activatedSkill.setGameInstance(gameInstance);
        this.activatedSkillRepository.save(activatedSkill);
    }

    private boolean isGameInstanceNotNight(GameInstance gameInstance) {
        return gameInstance.getGamePhase() != GamePhase.NIGHT;
    }
}
