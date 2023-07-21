package com.nsl.webmapia.gameoperation.service;

import com.nsl.webmapia.character.CharacterCode;
import com.nsl.webmapia.gameoperation.dto.PhaseEndNotificationDTO;
import com.nsl.webmapia.gameoperation.dto.VoteResultResponseDTO;
import com.nsl.webmapia.gameoperation.dto.CharacterGenerationResponseDTO;

import java.util.List;
import java.util.Map;

public interface GameService {

    /**
     * Generate characters and allocate each of the characters to each user in the game provided.
     * Inner state of each user instance stored in repository is mutated in this method.
     * @param gameId id of game.
     * @param characterDistribution key:value -> Character : # each of character to show up in this game.
     * @return information to be sent to each user saying that which character the user is allocated.
     *         notification type: enum(string), receiver id: number, character code: enum(string), game id: number
     *         will be specified.
     */
    List<CharacterGenerationResponseDTO> generateCharacters(Long gameId,
                                                            Map<CharacterCode, Integer> characterDistribution);

    /**
     * The phase of game steps forward.
     * @param gameId id of game.
     */
    void postPhase(Long gameId);

    /**
     * Accept vote of each user for execution.
     * @param gameId id of game.
     * @param voterId id of voter.
     * @param subjectId id of subject.
     */
    void acceptVote(Long gameId,
                    Long voterId, Long subjectId);

    /**
     * Given votes, determine which user has gotten the most votes such that he is supposed to be executed.
     * After the process of votes, the container of votes is going to get cleared.
     * @param gameId id of game.
     * @return DTO for notification of the result of the vote. If there exists tie such that the vote is invalid,
     *         idOfUserToBeExecuted field is set to null.
     */
    VoteResultResponseDTO processVotes(Long gameId);



    PhaseEndNotificationDTO phaseEnd(Long gameId, Long userId);
}
