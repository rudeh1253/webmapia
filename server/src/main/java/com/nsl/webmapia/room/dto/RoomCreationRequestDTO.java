package com.nsl.webmapia.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoomCreationRequestDTO {
    private String gameName;
    private Long hostId;
    private String hostName;
}
