package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.mapper.TwitchChannelMapper;
import ru.verpul.mapper.TwitchTokenMapper;
import ru.verpul.model.TwitchChannel;
import ru.verpul.model.TwitchToken;
import ru.verpul.repository.TwitchChannelRepository;
import ru.verpul.repository.TwitchTokenRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TwitchService {

    private final TwitchTokenRepository twitchTokenRepository;
    private final TwitchTokenMapper twitchTokenMapper;
    private final TwitchChannelRepository twitchChannelRepository;
    private final TwitchChannelMapper twitchChannelMapper;

    public TwitchTokenDTO getTwitchToken() {
        return twitchTokenMapper.twitchTokenToTwitchTokenDTO(twitchTokenRepository.getTwitchToken());
    }

    @Transactional
    public void saveToken(TwitchTokenDTO twitchTokenDTO) {
        TwitchToken tokenToSave = twitchTokenMapper.twitchTokenDTOToTwitchToken(twitchTokenDTO);
        TwitchToken savedToken = twitchTokenRepository.getTwitchToken();

        if (savedToken != null) {
            tokenToSave.setId(savedToken.getId());
        }

        twitchTokenRepository.save(tokenToSave);
    }

    public List<TwitchChannelDTO> getTwitchChannels() {
        return twitchChannelRepository.findAll().stream()
                .map(twitchChannelMapper::twitchChannelToTwitchChannelDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveChannel(TwitchChannelDTO twitchChannelDTO) {
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
        }
    }
}
