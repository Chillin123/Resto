package com.base.resto.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAuthorityUpdateNotification(String secureCode, String message) {
        messagingTemplate.convertAndSend("/topic/authority-updates/" + secureCode, message);
    }

    public void sendUpdateAuthorityRequestToAdmin(String message) {
        messagingTemplate.convertAndSend("/topic/authority-updates/admin", message);
    }
}
