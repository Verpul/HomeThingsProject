package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verpul.DTO.WeatherDTO;
import ru.verpul.data.WeatherData;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WeatherController {
    private final WeatherData weatherData;

    @GetMapping
    public WeatherDTO getWeather() {
        return weatherData.getCurrentWeatherData();
    }
}
