package com.example.backend.sse;

import com.example.backend.room.EnterRoomDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
public class SseController {

    private final SseEmitters sseEmitters;

    public SseController(SseEmitters sseEmitters) {
        this.sseEmitters = sseEmitters;
    }

    @CrossOrigin
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        // Set timeout to infinite
        SseEmitter emitter = new SseEmitter(-1L);

        EnterRoomDto enterRoomDto = new EnterRoomDto();
        // Set a temporary ID value for testing
        enterRoomDto.setUserId("1");

        sseEmitters.add(enterRoomDto.getUserId(), emitter);

        return ResponseEntity.ok(emitter);
    }
}
