package ru.verpul.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.service.TwitchAuthService;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TwitchSchedule {
    private final TwitchAuthService twitchAuthServiceService;

    @Scheduled(initialDelay = 2, fixedRate = 2, timeUnit = TimeUnit.HOURS)
    private void refreshToken() {
        if (twitchAuthServiceService.haveTwitchToken())
            twitchAuthServiceService.refreshToken();
    }

    @PostConstruct
    private void initTwitchClient() {
        refreshToken();
    }
}
