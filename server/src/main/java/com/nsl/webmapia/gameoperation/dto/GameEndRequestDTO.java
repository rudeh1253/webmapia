package com.nsl.webmapia.gameoperation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GameEndRequestDTO {
    Long gameId;
    Long userId;
}
