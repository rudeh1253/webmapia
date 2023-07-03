package com.nsl.webmapia.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Vote {
    private final int theNumberOfVote;
    private final User voter;
    private final User subject;
}
