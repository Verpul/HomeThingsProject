package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.service.TwitchService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/twitch", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TwitchController {
    private final TwitchService twitchService;

    @GetMapping("/token/authorized")
    public boolean getAuthorized() {
        return twitchService.getAuthorized();
    }

    @PostMapping("/token/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getNewTwitchToken(@PathVariable("code") String code) {
        twitchService.getNewTwitchToken(code);
    }

    @GetMapping("/followed")
    public List<TwitchChannelDTO> getFollowedChannels() {
        return twitchService.getFollowedChannels();
    }

    @GetMapping("/channel")
    public List<TwitchChannelDTO> getSavedTwitchChannels() {
        return twitchService.getTwitchChannels();
    }

    @PostMapping("/channel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveTwitchChannel(@RequestBody TwitchChannelDTO twitchChannelDTO) {
        twitchService.saveTwitchChannel(twitchChannelDTO);
    }
}
