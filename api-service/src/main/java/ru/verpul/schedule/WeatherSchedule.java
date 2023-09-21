package ru.verpul.schedule;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.DTO.WeatherDTO;
import ru.verpul.data.WeatherData;
import ru.verpul.feign.WeatherFeign;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherSchedule {
    private final WeatherFeign weatherFeign;
    private final WeatherData weatherData;

    @PostConstruct
    private void loadWeatherOnStartup() {
        this.loadWeatherData();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void loadWeatherData() {
        System.out.println("Я запустился");

        try {
            WeatherDTO loadedWeatherData = weatherFeign.getWeatherData();
            weatherData.setCurrentWeatherData(loadedWeatherData);
        } catch (FeignException e) {
            log.error("Error while loading weather data", e);
        }
    }
}
