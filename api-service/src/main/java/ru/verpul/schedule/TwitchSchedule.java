package ru.verpul.schedule;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.service.TwitchService;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TwitchSchedule {
    private final TwitchService twitchService;
    private final DiscoveryClient discoveryClient;

    private void startTwitchClient() {
        twitchService.validateTokenAndStartTwitchClient();
    }

    @Scheduled(initialDelay = 2, fixedRate = 2, timeUnit = TimeUnit.HOURS)
    private void refreshToken() {
        twitchService.refreshToken();
    }

    @PostConstruct
    private void initTwitchClient() {
        boolean localServiceDown = discoveryClient.getInstances("local-service").isEmpty();

        if (localServiceDown) {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::initTwitchClient, 10, TimeUnit.SECONDS);
        } else {
            startTwitchClient();
        }
    }
}
