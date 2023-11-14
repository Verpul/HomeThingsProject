package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class TwitchChannel extends BaseEntity{

    @Column(name = "channel_id", unique = true, nullable = false)
    private String channelId;

    @Column(name = "channel_name", unique = true, nullable = false)
    private String channelName;

    @Column(name = "went_live_notification", nullable = false)
    private Boolean wentLiveNotification;

    @Column(name = "changed_category_notification", nullable = false)
    private Boolean changedCategoryNotification;

}
