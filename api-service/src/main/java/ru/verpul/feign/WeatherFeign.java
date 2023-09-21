package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.verpul.DTO.WeatherDTO;

@FeignClient(name = "weather", url = "https://www.yr.no/api/v0/locations")
public interface WeatherFeign {
    @GetMapping("/2-524305/forecast")
    WeatherDTO getWeatherData();
}
