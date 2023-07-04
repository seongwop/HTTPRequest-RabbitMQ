package com.example.backend.rabbitmq;

import com.example.backend.sse.SseEmitters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Slf4j
@Service
public class EnterQueueService {

    private final RabbitTemplate rabbitTemplate;
    private final SseEmitters sseEmitters;

    public EnterQueueService(RabbitTemplate rabbitTemplate, SseEmitters sseEmitters) {
        this.rabbitTemplate = rabbitTemplate;
        this.sseEmitters = sseEmitters;
    }

    public void sendMessage(EnterQueueDto enterQueueDto) {

        enterQueueDto.setUuid(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend("enter_exchange", "enter_key", enterQueueDto);
        log.info("message sent by : {}", enterQueueDto.getUuid());
    }

    @RabbitListener(queues = "enter_queue", errorHandler = "customRabbitExceptionHandler")
    public void consumer1(EnterQueueDto enterQueueDto) {

        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
//                .name(enterQueueDto.getUserId())
                .data(enterQueueDto.getUuid());

        sseEmitters.send(enterQueueDto.getUserId(), sseEvent);
        log.info("consumer1 message received by : {}", enterQueueDto.getUuid());
    }

    @RabbitListener(queues = "enter_queue", errorHandler = "customRabbitExceptionHandler")
    public void consumer2(EnterQueueDto enterQueueDto) {

        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
//                .name(enterQueueDto.getUserId())
                .data(enterQueueDto.getUuid());

        sseEmitters.send(enterQueueDto.getUserId(), sseEvent);
        log.info("consumer2 message received by : {}", enterQueueDto.getUuid());
    }
}
