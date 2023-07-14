package com.example.backend.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseEmitters {

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(String id, SseEmitter emitter) {
        emitters.put(id, emitter);
        log.info("New emitter added: {}", emitter);
        log.info("Emitter list size: {}", emitters.size());

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitters.remove(id);
        });

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    @Async("threadPoolTaskExecutor")
    public void send(String id, SseEmitter.SseEventBuilder event) {
        SseEmitter emitter = emitters.get(id);

        if (emitter != null) {
            try {
                emitter.send(event);
                log.info("event sent to emiiter {}", emitter);
            } catch (IOException e) {
                log.error("Failed to send SSE event to emitter {}", emitter, e);
            }
        }
    }
}

