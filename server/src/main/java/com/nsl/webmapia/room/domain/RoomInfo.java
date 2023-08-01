package com.nsl.webmapia.room.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RoomInfo {
    private final Long gameId;
    private String gameName;
    private Long hostId;
    private List<User> users;
}
