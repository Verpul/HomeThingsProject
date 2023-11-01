package ru.verpul.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.feign.TwitchFeign;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitchService {

    @Value("${twitch.client-id}")
    private String clientId;

    @Value("${twitch.client-secret}")
    private String clientSecret;

    @Value("${twitch.grant-type}")
    private String grantType;

    @Value("${twitch.redirect-uri}")
    private String redirectUri;

    private final LocalServiceFeign localServiceFeign;
    private final TwitchFeign twitchFeign;

    public boolean getAuthorized() {
        TwitchTokenDTO token = null;
        try {
            token = localServiceFeign.getTwitchToken();
        } catch (FeignException e) {
            log.error("Не удалось загрузить данные twitch-токена из БД", e);
        }
        return token != null;
    }

    public void getNewTwitchToken(String code) {
        try {
            ResponseEntity<TwitchTokenDTO> responseNewToken = twitchFeign.getNewToken(clientId, clientSecret, code, grantType, redirectUri);
            if (responseNewToken.getStatusCodeValue() == 200) {
                TwitchTokenDTO newToken = responseNewToken.getBody();
                localServiceFeign.saveTwitchToken(newToken);
            }
        } catch (FeignException e) {
            log.error("Ошибка при получении нового токена", e);
        }
    }
}
