package com.nsl.webmapia.game.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
public class GameCreationRequestDTO {
    private String gameName;
}
