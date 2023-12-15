package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.YoutubeChannelDTO;
import ru.verpul.service.YoutubeService;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {
    private final YoutubeService youtubeService;

    @GetMapping("/follow")
    public List<YoutubeChannelDTO> getSubscriptions() {
        return youtubeService.getSubscriptions();
    }

    @GetMapping
    public List<YoutubeChannelDTO> getSavedChannels() {
        return youtubeService.getSavedChannels();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setChannelPreference(@RequestBody YoutubeChannelDTO channel) {
        youtubeService.setChannelPreferences(channel);
    }
}
