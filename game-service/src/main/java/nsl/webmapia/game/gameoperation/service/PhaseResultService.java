package nsl.webmapia.game.gameoperation.service;

import lombok.RequiredArgsConstructor;
import nsl.webmapia.game.gameoperation.domain.GamePhase;
import nsl.webmapia.game.gameoperation.dto.GameInstanceDto;
import nsl.webmapia.game.gameoperation.dto.response.GameResultResponseDto;
import nsl.webmapia.game.gameoperation.dto.response.PhaseResultResponseDto;
import nsl.webmapia.game.gameoperation.repository.PhaseEndRequestRepository;
import nsl.webmapia.game.gameroom.entity.Participation;
import nsl.webmapia.game.gameroom.repository.ParticipationRepository;
import nsl.webmapia.game.skill.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PhaseResultService {
    private final GameService gameService;
    private final SkillService skillService;
    private final PhaseEndRequestRepository phaseEndRequestRepository;
    private final ParticipationRepository participationRepository;
    private final VoteService voteService;

    /**
     * Request to end the phase. When all members request to end the phase, the phase ends.
     *
     * @param gameInstanceId of the GameInstance
     * @param requesterId    of Member who requested to end the phase
     * @return PhaseResultResponseDto that contains information of the result of the phase
     */
    public PhaseResultResponseDto endPhase(int gameInstanceId, String requesterId) {
        GameInstanceDto gameInstance = this.gameService.getGameInstance(gameInstanceId);
        Optional<Integer> phaseEndObjectiveOp =
                this.phaseEndRequestRepository.findPhaseEndObjectiveByGameInstanceId(gameInstanceId);
        if (phaseEndObjectiveOp.isEmpty()) {
            List<Participation> participations =
                    this.participationRepository.findNotDisconnectedByGameInstanceId(gameInstanceId);
            this.phaseEndRequestRepository.savePhaseEndObjective(gameInstanceId, participations.size());
        }
        this.phaseEndRequestRepository.savePhaseEndRequest(gameInstanceId, requesterId);
        boolean phaseEnd = this.phaseEndRequestRepository.findPhaseEndRequestsByGameInstanceId(gameInstanceId).size()
                == this.phaseEndRequestRepository.findPhaseEndObjectiveByGameInstanceId(gameInstanceId)
                .orElseThrow(IllegalArgumentException::new);
        if (!phaseEnd) {
            return PhaseResultResponseDto.builder()
                    .ended(false)
                    .build();
        }

        GamePhase currentPhase = gameInstance.getGamePhase();

        Object result = processPhaseResult(gameInstanceId, currentPhase);

        GamePhase nextPhase = this.gameService.proceedPhase(gameInstanceId);

        return PhaseResultResponseDto.builder()
                .ended(true)
                .currentPhase(gameInstance.getGamePhase())
                .nextPhase(nextPhase)
                .content(result)
                .build();
    }

    private Object processPhaseResult(int gameInstanceId, GamePhase gamePhase) {
        GameResultResponseDto gameResultResponseDto = this.gameService.processGameResult(gameInstanceId);
        if (gameResultResponseDto.isGameEnded()) {
            gamePhase = GamePhase.END;
        }
        return switch (gamePhase) {
            case START, DISCUSSION -> "";
            case NIGHT -> this.skillService.processSkills(gameInstanceId);
            case VOTE -> this.voteService.processVote(gameInstanceId);
            case END -> gameResultResponseDto;
        };
    }
}
