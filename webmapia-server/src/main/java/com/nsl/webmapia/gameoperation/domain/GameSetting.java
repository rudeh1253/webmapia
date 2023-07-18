package com.nsl.webmapia.gameoperation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameSetting {
    private long discussionTimeSeconds;
    private long voteTimeSeconds;
    private long nightTimeSeconds;
    private long maxCapacity;
}
