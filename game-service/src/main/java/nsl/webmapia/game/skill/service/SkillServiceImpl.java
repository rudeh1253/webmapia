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
import nsl.webmapia.game.skill.domain.SkillType;
import nsl.webmapia.game.skill.repository.ActivatedSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final ActivatedSkillRepository activatedSkillRepository;
    private final CharacterDefinitionFactoryService characterDefinitionFactory;
    private final CharacterAssignmentRepository characterAssignmentRepository;

    @Override
    public BaseSystemMessageResponseDto<List<SkillType>> getAvailableSkills(int gameRoomId, String memberId) {
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
        List<SkillType> result = characterDefinitionService.getAvailableSkillTypes(gameRoomId, memberId);
        return BaseSystemMessageResponseDto.<List<SkillType>>builder()
                .receiverIds(List.of(memberId))
                .systemMessageType(SystemMessageType.AVAILABLE_SKILLS)
                .content(result)
                .build();
    }
}
