package ru.verpul.service;

import com.github.twitch4j.helix.domain.OutboundFollowing;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.component.TwitchClientComponent;
import ru.verpul.mapper.TwitchChannelMapper;
import ru.verpul.model.TwitchChannel;
import ru.verpul.repository.TwitchChannelRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TwitchChannelService {

    @Value("${twitch.user-id}")
    private String userId;

    private final TwitchChannelRepository twitchChannelRepository;
    private final TwitchChannelMapper twitchChannelMapper;
    private final TwitchAuthService twitchAuthService;
    private final TwitchClientComponent twitchClientComponent;

    public TwitchChannelService(TwitchChannelRepository twitchChannelRepository, TwitchChannelMapper twitchChannelMapper,
                                TwitchAuthService twitchAuthService, @Lazy TwitchClientComponent twitchClientComponent) {
        this.twitchChannelRepository = twitchChannelRepository;
        this.twitchChannelMapper = twitchChannelMapper;
        this.twitchAuthService = twitchAuthService;
        this.twitchClientComponent = twitchClientComponent;
    }

    public List<TwitchChannelDTO> getSavedTwitchChannels() {
        return twitchChannelRepository.findAll().stream()
                .map(twitchChannelMapper::twitchChannelToTwitchChannelDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveTwitchChannel(TwitchChannelDTO twitchChannelDTO) {
        TwitchChannel foundChannel = twitchChannelRepository.findByChannelName(twitchChannelDTO.getChannelName())
                .orElse(twitchChannelMapper.twitchChannelDTOToTwitchChannel(twitchChannelDTO));

        if (foundChannel.getChannelId() != null
                && !twitchChannelDTO.getChangedCategoryNotification()
                && !twitchChannelDTO.getWentLiveNotification()) {
            twitchChannelRepository.delete(foundChannel);
        } else {
            if (foundChannel.getChannelId() != null) {
                foundChannel.setChangedCategoryNotification(twitchChannelDTO.getChangedCategoryNotification());
                foundChannel.setWentLiveNotification(twitchChannelDTO.getWentLiveNotification());
            }

            twitchChannelRepository.save(foundChannel);
            twitchAuthService.validateTokenAndStartTwitchClient();
        }
    }

    public List<TwitchChannelDTO> getFollowedChannels() {
        OutboundFollowing followedChannels = twitchClientComponent
                .getTwitchClient()
                .getHelix()
                .getFollowedChannels(twitchAuthService.getTwitchToken().getAccessToken(), userId, null, 100, null)
                .execute();

        return Objects.requireNonNull(followedChannels.getFollows()).stream()
                .map(followedChannel -> {
                    TwitchChannelDTO channel = new TwitchChannelDTO();
                    channel.setChannelId(followedChannel.getBroadcasterId());
                    channel.setChannelName(followedChannel.getBroadcasterLogin());

                    return channel;
                }).collect(Collectors.toList());
    }
}
