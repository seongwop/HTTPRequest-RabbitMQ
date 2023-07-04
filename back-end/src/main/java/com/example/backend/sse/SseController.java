package com.example.backend.sse;

import com.example.backend.rabbitmq.EnterQueueDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        SseEmitter emitter = new SseEmitter(120000L);
        EnterQueueDto enterQueueDto = new EnterQueueDto();
        enterQueueDto.setUserId("1");

        sseEmitters.add(enterQueueDto.getUserId(), emitter);

        return ResponseEntity.ok(emitter);
    }

//    @CrossOrigin
//    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter connect() {
//        SseEmitter emitter = new SseEmitter();
//        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
//        sseMvcExecutor.execute(() -> {
//            try {
//                for (int i = 0; i < 20; i++) {
//                    SseEmitter.SseEventBuilder event = SseEmitter.event()
//                            .data(System.currentTimeMillis());
//                    emitter.send(event);
//                    Thread.sleep(1000);
//                }
//            } catch (Exception ex) {
//                emitter.completeWithError(ex);
//            }
//        });
//        return emitter;
//    }

}
