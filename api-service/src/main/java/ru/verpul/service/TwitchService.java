package ru.verpul.service;

import com.github.twitch4j.helix.domain.OutboundFollowing;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.component.TwitchClientComponent;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.feign.TwitchFeign;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitchService {
    @Value("${twitch.user-id}")
    private String userId;

    @Value("${twitch.client-id}")
    private String clientId;

    @Value("${twitch.client-secret}")
    private String clientSecret;

    @Value("${twitch.redirect-uri}")
    private String redirectUri;

    private final LocalServiceFeign localServiceFeign;
    private final TwitchFeign twitchFeign;
    private final TwitchClientComponent twitchClientComponent;

    public void validateTokenAndStartTwitchClient() {
        if (haveTwitchToken()) {
            if (!validateToken(localServiceFeign.getTwitchToken()))  {
                refreshToken();
            }
            twitchClientComponent.start();
        }
    }

    public boolean haveTwitchToken() {
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
            ResponseEntity<TwitchTokenDTO> responseNewToken = twitchFeign.getNewToken(clientId, clientSecret, code, "authorization_code", redirectUri);

            if (responseNewToken.getStatusCodeValue() == 200) {
                TwitchTokenDTO newToken = responseNewToken.getBody();
                localServiceFeign.saveTwitchToken(newToken);
                twitchClientComponent.start();
            }
        } catch (FeignException e) {
            log.error("Ошибка при получении нового токена", e);
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
            TwitchTokenDTO token = localServiceFeign.getTwitchToken();
            ResponseEntity<TwitchTokenDTO> response = twitchFeign.refreshToken(clientId, clientSecret, "refresh_token", token.getRefreshToken());

            if (response.getStatusCodeValue() == 200) {
                TwitchTokenDTO refreshedToken = response.getBody();
                localServiceFeign.saveTwitchToken(refreshedToken);
            }
        } catch (FeignException e) {
            log.error("Не удалось обновить токен", e);
        }
    }

    public List<TwitchChannelDTO> getFollowedChannels() {
        OutboundFollowing followedChannels = twitchClientComponent
                .getTwitchClient()
                .getHelix()
                .getFollowedChannels(localServiceFeign.getTwitchToken().getAccessToken(), userId, null, 100, null)
                .execute();

        return Objects.requireNonNull(followedChannels.getFollows()).stream()
                .map(followedChannel -> {
                    TwitchChannelDTO channel = new TwitchChannelDTO();
                    channel.setChannelId(followedChannel.getBroadcasterId());
                    channel.setChannelName(followedChannel.getBroadcasterName());

                    return channel;
                }).collect(Collectors.toList());
    }


    public List<TwitchChannelDTO> getTwitchChannels() {
        try {
            return localServiceFeign.getTwitchChannels();
        } catch (FeignException e) {
            log.error("Не удалось загрузить twitch-каналы", e);
        }

        return null;
    }

    public void saveTwitchChannel(TwitchChannelDTO twitchChannelDTO) {
        try {
            localServiceFeign.saveTwitchChannel(twitchChannelDTO);
            validateTokenAndStartTwitchClient();
        } catch (FeignException e) {
            log.error("Не удалось сохранить twitch-канал", e);
        }
    }
}
