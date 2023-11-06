package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.TwitchTokenDTO;
import ru.verpul.model.TwitchToken;

@Mapper(componentModel = "spring")
public interface TwitchTokenMapper {
    TwitchToken twitchTokenDTOToTwitchToken(TwitchTokenDTO twitchTokenDTO);

    TwitchTokenDTO twitchTokenToTwitchTokenDTO(TwitchToken twitchToken);
}
