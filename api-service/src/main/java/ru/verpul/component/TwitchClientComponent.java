package ru.verpul.component;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.feign.LocalServiceFeign;

@Component
@RequiredArgsConstructor
@Getter
public class TwitchClientComponent {

    private TwitchClient twitchClient;

    private final LocalServiceFeign localServiceFeign;

    public void start() {
        if (twitchClient != null) twitchClient.close();

        TwitchTokenDTO accessToken = localServiceFeign.getTwitchToken();
        OAuth2Credential credential = new OAuth2Credential("twitch", accessToken.getAccessToken());

        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                .build();

//        twitchClient.getClientHelper().enableStreamEventListener()

    }
}
