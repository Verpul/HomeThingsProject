package ru.verpul.data;

import org.springframework.stereotype.Component;
import ru.verpul.DTO.WeatherDTO;

@Component
public class WeatherData {
    private static WeatherDTO weatherDTO;

    public WeatherDTO getCurrentWeatherData() {
        return weatherDTO;
    }

    public void setCurrentWeatherData(WeatherDTO weatherData) {
        weatherDTO = weatherData;
    }
}
