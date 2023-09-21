package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import ru.verpul.util.WeatherUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {

    private String updatedDateTime;

    private List<Map<String, Object>> dayIntervals = new ArrayList<>();

    private List<Map<String, String>> shortIntervals = new ArrayList<>();

    @JsonProperty("created")
    private void unpackCreatedFieldData(JsonNode createdNode) {
        this.updatedDateTime = WeatherUtil.getFormattedWeatherUpdateDate(createdNode.asText());
    }

    @JsonProperty("dayIntervals")
    private void unpackDayIntervalsData(JsonNode dayIntervalsNode) {
        for (JsonNode dayInterval : dayIntervalsNode) {
            Map<String, Object> dayIntervalMap = new HashMap<>();

            String start = WeatherUtil.getFormattedWeatherDate(dayInterval.get("start").asText());
            String temperatureMin = WeatherUtil.roundAndFormatNumber(dayInterval.get("temperature").get("min").asDouble());
            String temperatureMax = WeatherUtil.roundAndFormatNumber(dayInterval.get("temperature").get("max").asDouble());
            String precipitation = dayInterval.get("precipitation").get("value").asText();
            String wind = WeatherUtil.roundAndFormatNumber(dayInterval.get("wind").get("max").asDouble());
            List<String> sixHoursSymbols = new ArrayList<>();
            dayInterval.get("sixHourSymbols").forEach(element -> sixHoursSymbols.add(element.asText()));

            dayIntervalMap.put("start", start);
            dayIntervalMap.put("temperatureMin", temperatureMin);
            dayIntervalMap.put("temperatureMax", temperatureMax);
            dayIntervalMap.put("precipitation", precipitation);
            dayIntervalMap.put("wind", wind);
            dayIntervalMap.put("sixHoursSymbols", sixHoursSymbols);

            dayIntervals.add(dayIntervalMap);
        }
    }

    @JsonProperty("shortIntervals")
    private void unpackShortIntervalsData(JsonNode shortIntervalsNode) {
        for (JsonNode shortInterval : shortIntervalsNode) {
            Map<String, String> shortIntervalsMap = new HashMap<>();

            String start = WeatherUtil.getFormattedWeatherDate(shortInterval.get("start").asText());
            String time = WeatherUtil.getWeatherTime(shortInterval.get("start").asText());
            String icon = shortInterval.get("symbolCode").get("next1Hour").asText();
            String temperature = WeatherUtil.roundAndFormatNumber(shortInterval.get("temperature").get("value").asDouble());
            String feelsLike = WeatherUtil.roundAndFormatNumber(shortInterval.get("feelsLike").get("value").asDouble());
            String precipitationMin = shortInterval.get("precipitation").get("min").asText();
            String precipitationMax = shortInterval.get("precipitation").get("max").asText();
            String precipitationAverage = shortInterval.get("precipitation").get("value").asText();
            String windAverage = WeatherUtil.roundAndFormatNumber(shortInterval.get("wind").get("speed").asDouble());
            String windGusts = WeatherUtil.roundAndFormatNumber(shortInterval.get("wind").get("gust").asDouble());

            shortIntervalsMap.put("start", start);
            shortIntervalsMap.put("time", time);
            shortIntervalsMap.put("icon", icon);
            shortIntervalsMap.put("temperature", temperature);
            shortIntervalsMap.put("feelsLike", feelsLike);
            shortIntervalsMap.put("precipitationMin", precipitationMin.equals("0.0") ? "0" : precipitationMin);
            shortIntervalsMap.put("precipitationMax", precipitationMax);
            shortIntervalsMap.put("precipitationAverage", precipitationAverage.equals("0.0") ? "0" : precipitationMin);
            shortIntervalsMap.put("windAverage", windAverage);
            shortIntervalsMap.put("windGusts", windGusts);

            shortIntervals.add(shortIntervalsMap);
        }
    }
}
