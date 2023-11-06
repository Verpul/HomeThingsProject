package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.verpul.DTO.TwitchTokenDTO;

@FeignClient(name = "twitch", url = "https://id.twitch.tv/oauth2")
public interface TwitchFeign {

    @PostMapping("/token")
    ResponseEntity<TwitchTokenDTO> getNewToken(@RequestParam("client_id") String clientId,
                                               @RequestParam("client_secret") String clientSecret,
                                               @RequestParam String code,
                                               @RequestParam("grant_type") String grantType,
                                               @RequestParam("redirect_uri") String redirectUri);

    @GetMapping("/validate")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);

    @PostMapping("/token")
    ResponseEntity<TwitchTokenDTO> refreshToken(@RequestParam("client_id") String clientId,
                                                @RequestParam("client_secret") String clientSecret,
                                                @RequestParam("grant_type") String grantType,
                                                @RequestParam("refresh_token") String refreshToken);
}
