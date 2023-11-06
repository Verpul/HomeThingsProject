package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("tg-bot-service")
public interface TGBotFeign {

    @PostMapping("/api/tg/twitch")
    void sendTGNotification(@RequestBody String message);
}
