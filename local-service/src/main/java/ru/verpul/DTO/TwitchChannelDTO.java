package ru.verpul.DTO;

import lombok.Data;

@Data
public class TwitchChannelDTO {
    private String channelId;
    private String channelName;
    private Boolean wentLiveNotification;
    private Boolean changedCategoryNotification;
}
