package com.example.backend.room;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnterRoomController {

    private final EnterRoomService enterRoomService;

    public EnterRoomController(EnterRoomService enterRoomService) {
        this.enterRoomService = enterRoomService;
    }

    @PostMapping("/enter")
    public void enterRoom(@RequestBody EnterRoomDto enterRoomDto) {
        enterRoomService.enterRoom(enterRoomDto);
    }

    @PostMapping("/enter-queue")
    public void enterQueue(@RequestBody EnterRoomDto enterRoomDto) {
        enterRoomService.enterQueue(enterRoomDto);
    }

    @PostMapping("/room")
    public void makeRoom() {
        enterRoomService.makeRoom();
    }
}
