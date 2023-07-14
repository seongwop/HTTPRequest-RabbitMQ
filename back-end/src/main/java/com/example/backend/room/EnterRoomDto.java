package com.example.backend.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterRoomDto {
    private Long roomId;
    private String userId;
    private String uuid;
}
