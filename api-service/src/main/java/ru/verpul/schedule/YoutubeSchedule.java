package ru.verpul.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.service.YoutubeService;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class YoutubeSchedule {
    private final YoutubeService youtubeService;

    @Scheduled(fixedRate = 12, timeUnit = TimeUnit.HOURS)
    public void checkNewVideos() {
        youtubeService.checkNewVideos();
    }
}
