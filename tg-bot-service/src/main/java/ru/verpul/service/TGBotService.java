package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verpul.component.HomeThingsBot;

@Service
@RequiredArgsConstructor
public class TGBotService {
    private final HomeThingsBot homeThingsBot;

    public void sendNotification(String message) {
        homeThingsBot.sendMessage(message);
    }
}
