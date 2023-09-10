package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.character.Faction;
import com.nsl.webmapia.common.NotificationType;
import com.nsl.webmapia.gameoperation.domain.GamePhase;
import com.nsl.webmapia.skill.dto.SkillResultResponseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class PhaseResultResponseDTO {
    private NotificationType notificationType;
    private Long gameId;
    private boolean isGameEnd;
    private Faction winner;
    private GamePhase endedPhase;
    private List<SkillResultResponseDTO> skillResults;
    private VoteResultResponseDTO voteResult;

    public static PhaseResultResponseDTO of(Long gameId,
                                            boolean isGameEnd,
                                            Faction winner,
                                            GamePhase endedPhase) {
        return PhaseResultResponseDTO.builder()
                .notificationType(NotificationType.PHASE_RESULT)
                .gameId(gameId)
                .isGameEnd(isGameEnd)
                .winner(winner)
                .endedPhase(endedPhase)
                .skillResults(new ArrayList<>())
                .voteResult(VoteResultResponseDTO.from(NotificationType.INVALID_VOTE, gameId, null))
                .build();
    }

    public void addSkillResult(SkillResultResponseDTO result) {
        this.skillResults.add(result);
    }

    public void setVoteResult(VoteResultResponseDTO result) {
        this.voteResult = result;
    }
}
