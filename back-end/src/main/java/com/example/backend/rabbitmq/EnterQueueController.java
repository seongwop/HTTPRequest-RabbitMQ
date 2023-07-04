package com.example.backend.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnterQueueController {

    private final EnterQueueService enterQueueService;

    public EnterQueueController(EnterQueueService enterQueueService) {
        this.enterQueueService = enterQueueService;
    }

    @PostMapping("/enter")
    public void enterQueue(@RequestBody EnterQueueDto enterQueueDto) {
        enterQueueService.sendMessage(enterQueueDto);
    }
}
