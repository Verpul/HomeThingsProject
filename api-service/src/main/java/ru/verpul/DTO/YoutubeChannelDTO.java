package ru.verpul.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YoutubeChannelDTO {
    private String thumbnailUrl;
    private String channelName;
    private String channelId;
    private boolean checkNewVideo;
}
