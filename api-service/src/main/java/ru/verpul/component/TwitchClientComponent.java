package ru.verpul.component;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import feign.FeignException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.enums.TwitchEvent;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.feign.TGBotFeign;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class TwitchClientComponent {

    private TwitchClient twitchClient;

    private final TGBotFeign tgBotFeign;
    private final LocalServiceFeign localServiceFeign;

    public void start() {
        if (this.twitchClient != null) twitchClient.close();

        try {
            TwitchTokenDTO accessToken = localServiceFeign.getTwitchToken();
            OAuth2Credential credential = new OAuth2Credential("twitch", accessToken.getAccessToken());

            twitchClient = TwitchClientBuilder.builder()
                    .withDefaultAuthToken(credential)
                    .withEnableHelix(true)
                    .build();


            List<TwitchChannelDTO> savedChannels = localServiceFeign.getTwitchChannels();
            List<String> savedChannelsNames = savedChannels.stream()
                    .map(TwitchChannelDTO::getChannelName)
                    .collect(Collectors.toList());

            twitchClient.getClientHelper().enableStreamEventListener(savedChannelsNames);

            twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, (channelGoLiveEvent) -> {
                String channelName = channelGoLiveEvent.getChannel().getName();

                boolean channelGoLiveTrigger = savedChannels.stream()
                        .anyMatch(channel -> channel.getChannelName().equals(channelName) && channel.getWentLiveNotification());

                if (channelGoLiveTrigger) {
                    String message = channelGoLiveEvent.getChannel().getName() + " начал трансляцию";
                    tgBotFeign.sendTGNotification(message);
                }
            });

            twitchClient.getEventManager().onEvent(ChannelChangeGameEvent.class, channelChangeGameEvent -> {
                String channelName = channelChangeGameEvent.getChannel().getName();

                boolean channelChangeGameEventTrigger = savedChannels.stream()
                        .anyMatch(channel -> channel.getChannelName().equals(channelName) && channel.getChangedCategoryNotification());

                if (channelChangeGameEventTrigger) {
                    String message = channelName + " сменил категорию на " + channelChangeGameEvent.getStream().getGameName();
                    tgBotFeign.sendTGNotification(message);
                }
            });

        } catch (FeignException e) {
            log.error("Не удалось запустить twitch client", e);
        }
    }
}
