package com.nsl.webmapia.gameoperation.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Vote {
    private final int theNumberOfVote;
    private final User voter;
    private final User subject;
}
