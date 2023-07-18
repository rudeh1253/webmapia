package com.nsl.webmapia.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomCreationRequestDTO {
    private String gameName;
    private Long hostId;
}
