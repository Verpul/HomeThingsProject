package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.TwitchChannelDTO;
import ru.verpul.model.TwitchChannel;

@Mapper(componentModel = "spring")
public interface TwitchChannelMapper {
    TwitchChannel twitchChannelDTOToTwitchChannel(TwitchChannelDTO twitchChannelDTO);

    TwitchChannelDTO twitchChannelToTwitchChannelDTO(TwitchChannel twitchChannel);
}
