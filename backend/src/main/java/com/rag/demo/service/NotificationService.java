package com.rag.demo.service;

import com.rag.demo.dto.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void send(String message, String topic) {
        final Notification msg = new Notification(message, false, false, topic);
        messagingTemplate.convertAndSend(String.format("/topic/%s", topic), msg);
    }

    public void send(Notification notification) {
        messagingTemplate.convertAndSend(String.format("/topic/%s", notification.topic()), notification);
    }

    public void sendError(String message, String topic) {
        final Notification msg = new Notification(message, true, false, topic);
        messagingTemplate.convertAndSend(String.format("/topic/%s", topic), msg);
    }

}