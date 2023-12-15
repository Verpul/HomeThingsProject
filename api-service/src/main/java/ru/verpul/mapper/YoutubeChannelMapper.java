package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.YoutubeChannelDTO;
import ru.verpul.model.YoutubeChannel;

@Mapper(componentModel = "spring")
public interface YoutubeChannelMapper {
    YoutubeChannel youtubeChannelDTOToYoutubeChannel(YoutubeChannelDTO youtubeChannelDTO);

    YoutubeChannelDTO youtubeChannelToYoutubeChannelDTO(YoutubeChannel youtubeChannel);
}
