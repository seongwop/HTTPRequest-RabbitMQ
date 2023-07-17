package com.example.backend.rabbitmq;

import com.example.backend.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRabbitExceptionHandler implements RabbitListenerErrorHandler {

    private final SseEmitters sseEmitters;

    // To catch the exception occurred from consumer for sending the message to user via SSE
    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {

        log.error("error occurred : {}",  exception.getMessage());

        String userId = amqpMessage.getMessageProperties().getHeader("userId");

        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
                .data("Room is over occupied");

        sseEmitters.send(userId, sseEvent);

        return null;
    }
}