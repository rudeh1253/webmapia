package com.nsl.webmapia.gameoperation.service;

import com.nsl.webmapia.character.*;
import com.nsl.webmapia.gameoperation.domain.GameManager;
import com.nsl.webmapia.gameoperation.domain.GamePhase;
import com.nsl.webmapia.gameoperation.dto.PhaseEndResponseDTO;
import com.nsl.webmapia.gameoperation.dto.PhaseResultResponseDTO;
import com.nsl.webmapia.gameoperation.dto.VoteResultResponseDTO;
import com.nsl.webmapia.skill.domain.SkillType;
import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import com.nsl.webmapia.skill.service.SkillService;
import com.nsl.webmapia.user.domain.User;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.repository.GameRepository;
import com.nsl.webmapia.gameoperation.dto.CharacterGenerationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements  GameService {
    private final GameRepository gameRepository;
    private final SkillService skillService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, SkillService skillService) {
        this.gameRepository = gameRepository;
        this.skillService = skillService;
    }

    @Override
    public List<CharacterGenerationResponseDTO> generateCharacters(Long gameId,
                                                                   Map<CharacterCode, Integer> characterDistribution) {
        GameManager gameManager = findGameManager(gameId);
        final List<User> users = gameManager.generateCharacters(characterDistribution);
        final List<CharacterGenerationResponseDTO> dtoList = new ArrayList<>();

        users.forEach(user ->
                dtoList.add(CharacterGenerationResponseDTO.from(NotificationType.NOTIFY_WHICH_CHARACTER_ALLOCATED,
                        user.getID(),
                        user.getCharacter().getCharacterCode(),
                        gameId)));
        return dtoList;
    }

    @Override
    public PhaseResultResponseDTO postPhase(Long gameId) {
        GameManager game = findGameManager(gameId);
        Faction winner = game.postPhase();
        game.getAllUsers().forEach(user -> user.setPhaseEnd(false));
        return PhaseResultResponseDTO.of(gameId, winner != null, winner);
    }

    @Override
    public void acceptVote(Long gameId, Long voterId, Long subjectId) {
        GameManager game = findGameManager(gameId);
        game.acceptVote(voterId, subjectId);
    }

    @Override
    public VoteResultResponseDTO processVotes(Long gameId) {
        GameManager game = findGameManager(gameId);
        User mostUser = game.processVotes();
        return mostUser == null
                ? VoteResultResponseDTO.from(NotificationType.INVALID_VOTE, gameId, null)
                : VoteResultResponseDTO.from(NotificationType.EXECUTE_BY_VOTE, gameId, mostUser.getID());
    }

    @Override
    public void activateSkill(Long gameId, Long activatorId, Long targetId, SkillType skillType) {
        skillService.activateSkill(gameId, activatorId, targetId, skillType);
    }

    @Override
    public List<SkillResultResponseDTO> processSkills(Long gameId) {
        List<SkillResultResponseDTO> result = skillService.processSkills(gameId);
        return result;
    }

    @Override
    public PhaseEndResponseDTO phaseEnd(Long gameId, Long userId) {
        GameManager game = findGameManager(gameId);
        boolean isEnd = game.endPhase(userId);
        return PhaseEndResponseDTO.from(gameId, isEnd);
    }

    private GameManager findGameManager(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }

    @Override
    public GamePhase getCurrentPhase(Long gameId) {
        GameManager game = findGameManager(gameId);
        return game.currentPhase();
    }
}
