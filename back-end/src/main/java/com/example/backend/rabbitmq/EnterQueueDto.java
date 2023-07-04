package com.example.backend.rabbitmq;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class EnterQueueDto {

    private String userId;
    private String uuid;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
