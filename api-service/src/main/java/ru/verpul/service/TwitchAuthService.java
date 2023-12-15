package ru.verpul.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.component.TwitchClientComponent;
import ru.verpul.feign.TwitchFeign;
import ru.verpul.mapper.TwitchTokenMapper;
import ru.verpul.model.TwitchToken;
import ru.verpul.repository.TwitchTokenRepository;

@Service
@Slf4j
public class TwitchAuthService {

    @Value("${twitch.client-id}")
    private String clientId;

    @Value("${twitch.client-secret}")
    private String clientSecret;

    @Value("${twitch.redirect-uri}")
    private String redirectUri;

    private final TwitchTokenRepository twitchTokenRepository;
    private final TwitchTokenMapper twitchTokenMapper;
    private final TwitchFeign twitchFeign;
    private final TwitchClientComponent twitchClientComponent;

    public TwitchAuthService(TwitchTokenRepository twitchTokenRepository, TwitchTokenMapper twitchTokenMapper,
                             TwitchFeign twitchFeign, @Lazy TwitchClientComponent twitchClientComponent) {
        this.twitchTokenRepository = twitchTokenRepository;
        this.twitchTokenMapper = twitchTokenMapper;
        this.twitchFeign = twitchFeign;
        this.twitchClientComponent = twitchClientComponent;
    }

    public boolean haveTwitchToken() {
        return twitchTokenRepository.getTwitchToken() != null;
    }

    public TwitchToken getTwitchToken() {
        return twitchTokenRepository.getTwitchToken();
    }

    public void validateTokenAndStartTwitchClient() {
        if (haveTwitchToken()) {
            if (!validateToken(twitchTokenMapper.twitchTokenToTwitchTokenDTO(twitchTokenRepository.getTwitchToken())))  {
                refreshToken();
            }
            twitchClientComponent.start();
        }
    }

    private boolean validateToken(TwitchTokenDTO token) {
        try {
            ResponseEntity<String> response = twitchFeign.validateToken("Bearer " + token.getAccessToken());

            if (response.getStatusCodeValue() == 200) {
                return true;
            }
        } catch (FeignException e) {
            log.error("Не удалось валидировать токен", e);
        }

        return false;
    }

    public void refreshToken() {
        try {
            TwitchToken token = twitchTokenRepository.getTwitchToken();
            ResponseEntity<TwitchTokenDTO> response = twitchFeign.refreshToken(clientId, clientSecret, "refresh_token", token.getRefreshToken());

            if (response.getStatusCodeValue() == 200) {
                TwitchTokenDTO refreshedToken = response.getBody();
                saveTwitchToken(refreshedToken);
                twitchClientComponent.start();
            }
        } catch (FeignException e) {
            log.error("Не удалось обновить токен", e);
        }
    }

    public void getAndSaveNewTwitchToken(String code) {
        try {
            ResponseEntity<TwitchTokenDTO> responseNewToken = twitchFeign.getNewToken(clientId, clientSecret, code, "authorization_code", redirectUri);

            if (responseNewToken.getStatusCodeValue() == 200) {
                TwitchTokenDTO newToken = responseNewToken.getBody();
                saveTwitchToken(newToken);
                twitchClientComponent.start();
            }
        } catch (FeignException e) {
            log.error("Ошибка при получении нового токена", e);
        }
    }

    public void saveTwitchToken(TwitchTokenDTO twitchTokenDTO) {
        TwitchToken tokenToSave = twitchTokenMapper.twitchTokenDTOToTwitchToken(twitchTokenDTO);
        TwitchToken savedToken = twitchTokenRepository.getTwitchToken();

        if (savedToken != null) {
            tokenToSave.setId(savedToken.getId());
        }

        twitchTokenRepository.save(tokenToSave);
    }
}
