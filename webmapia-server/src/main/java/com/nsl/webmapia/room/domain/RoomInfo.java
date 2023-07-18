package com.nsl.webmapia.room.domain;

import com.nsl.webmapia.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomInfo {
    private final Long gameId;
    private String gameName;
    private Long hostId;
    private List<User> users;

    public RoomInfo(Long gameId, String gameName, Long hostId, List<User> users) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.hostId = hostId;
        this.users = users;
    }
}
