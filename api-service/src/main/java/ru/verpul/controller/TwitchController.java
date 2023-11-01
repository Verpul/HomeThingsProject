package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.service.TwitchService;

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
    public void getNewTwitchToken(@PathVariable("code") String code) {
        twitchService.getNewTwitchToken(code);
    }
}
