package com.example.backend.room;

import com.example.backend.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterRoomService {

    private final RabbitTemplate rabbitTemplate;
    private final SseEmitters sseEmitters;
    private final RoomRepository roomRepository;

    @Transactional
    public void enterRoom(EnterRoomDto enterRoomDto) {
        Room room = roomRepository.findByRoomId(enterRoomDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("No room found")
        );
        room.setUserCount(room.getUserCount() + 1);

        if (room.getUserCount() > 10) throw new IllegalArgumentException("Room is over occupied");
    }

    public void enterQueue(EnterRoomDto enterRoomDto) {

        enterRoomDto.setUuid(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend("enter_exchange", "enter_key", enterRoomDto);
        log.info("message sent by : {}", enterRoomDto.getUuid());
    }

    @Transactional
    @RabbitListener(queues = "enter_queue", errorHandler = "customRabbitExceptionHandler")
    public void consumer(EnterRoomDto enterRoomDto, Message message) {
        message.getMessageProperties().setHeader("userId", enterRoomDto.getUserId());

        Room room = roomRepository.findByRoomId(enterRoomDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("No room found")
        );

        room.setUserCount(room.getUserCount() + 1);

        if (room.getUserCount() > 10) throw new IllegalArgumentException("Room is over occupied");

        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
                .data(enterRoomDto.getUuid());

        sseEmitters.send(enterRoomDto.getUserId(), sseEvent);
        log.info("consumer message received by : {}", enterRoomDto.getUuid());
    }

    public void makeRoom() {
        roomRepository.save(new Room());
    }
}
