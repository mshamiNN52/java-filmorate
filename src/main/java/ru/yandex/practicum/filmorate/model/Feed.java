package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Builder
public class Feed {
    private int id;
    private LocalDateTime timeStamp;
    private int userId;
    private String eventType;
    private String operation;
    private int entityId;


    public Feed(int id, LocalDateTime timeStamp, int userId, String eventType, String operation, int entityId) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
