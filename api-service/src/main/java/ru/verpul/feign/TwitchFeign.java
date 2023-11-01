package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.verpul.DTO.TwitchTokenDTO;

@FeignClient(name = "twitch", url = "https://id.twitch.tv/oauth2/token")
public interface TwitchFeign {

    @PostMapping
    ResponseEntity<TwitchTokenDTO> getNewToken(@RequestParam(value = "client_id") String clientId,
                                               @RequestParam(value = "client_secret") String clientSecret,
                                               @RequestParam String code,
                                               @RequestParam(value = "grant_type") String grant_type,
                                               @RequestParam(value = "redirect_uri") String redirectUri);
}
