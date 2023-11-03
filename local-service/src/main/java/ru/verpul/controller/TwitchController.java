package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.service.TwitchService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/db/twitch", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TwitchController {

    private final TwitchService twitchService;

    @GetMapping("/token")
    public TwitchTokenDTO getTwitchToken() {
        return twitchService.getTwitchToken();
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveToken(@RequestBody TwitchTokenDTO twitchTokenDTO) {
        twitchService.saveToken(twitchTokenDTO);
    }

    @GetMapping("/channel")
    public List<TwitchChannelDTO> getSavedTwitchChannels() {
        return twitchService.getTwitchChannels();
    }

    @PostMapping("/channel")
    public void saveChannel(@RequestBody TwitchChannelDTO twitchChannelDTO) {
        twitchService.saveChannel(twitchChannelDTO);
    }
}
