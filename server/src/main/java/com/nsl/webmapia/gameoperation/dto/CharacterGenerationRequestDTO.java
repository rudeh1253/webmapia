package com.nsl.webmapia.gameoperation.dto;

import com.nsl.webmapia.character.CharacterCode;
import com.nsl.webmapia.common.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class CharacterGenerationRequestDTO {
    private final Long gameId;
    private final Map<CharacterCode, Integer> characterDistribution;
}
