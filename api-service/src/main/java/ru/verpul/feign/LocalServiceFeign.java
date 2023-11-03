package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.DTO.TwitchTokenDTO;

import java.util.List;

@FeignClient("local-service")
public interface LocalServiceFeign {

    @GetMapping("/api/db/twitch/token")
    TwitchTokenDTO getTwitchToken();

    @PostMapping("/api/db/twitch/token")
    void saveTwitchToken(@RequestBody TwitchTokenDTO twitchTokenDTO);

    @GetMapping("/api/db/twitch/channel")
    List<TwitchChannelDTO> getTwitchChannels();

    @PostMapping("/api/db/twitch/channel")
    void saveTwitchChannel(@RequestBody TwitchChannelDTO twitchChannelDTO);
}
