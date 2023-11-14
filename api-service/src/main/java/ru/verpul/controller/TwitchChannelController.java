package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.service.TwitchChannelService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/twitch/channel", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TwitchChannelController {

    private final TwitchChannelService twitchChannelService;

    @GetMapping("/follow")
    public List<TwitchChannelDTO> getFollowedChannels() {
        return twitchChannelService.getFollowedChannels();
    }

    @GetMapping
    public List<TwitchChannelDTO> getSavedTwitchChannels() {
        return twitchChannelService.getSavedTwitchChannels();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveTwitchChannel(@RequestBody TwitchChannelDTO twitchChannelDTO) {
        twitchChannelService.saveTwitchChannel(twitchChannelDTO);
    }

}
