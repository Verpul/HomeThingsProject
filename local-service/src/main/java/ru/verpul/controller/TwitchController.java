package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.model.TwitchToken;
import ru.verpul.service.TwitchService;

@RestController
@RequestMapping(value = "/api/db/twitch", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TwitchController {

    private final TwitchService twitchService;

    @GetMapping("/token")
    public TwitchToken getTwitchToken() {
        return twitchService.getTwitchToken();
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveToken(@RequestBody TwitchToken twitchToken) {
        twitchService.saveToken(twitchToken);
    }

}
