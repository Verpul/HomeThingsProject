package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.service.TwitchAuthService;

@RestController
@RequestMapping(value = "/api/twitch/token", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TwitchAuthController {
    private final TwitchAuthService twitchAuthService;

    @GetMapping("/authorized")
    public boolean getAuthorized() {
        return twitchAuthService.haveTwitchToken();
    }

    @PostMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getAndSaveNewTwitchToken(@PathVariable("code") String code) {
        twitchAuthService.getAndSaveNewTwitchToken(code);
    }

}
