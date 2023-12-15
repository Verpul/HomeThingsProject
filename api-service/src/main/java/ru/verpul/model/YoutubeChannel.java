package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "youtube_channel")
@Getter
@Setter
public class YoutubeChannel extends BaseEntity{
    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "channel_name", nullable = false, unique = true)
    private String channelName;

    @Column(name = "channel_id", nullable = false, unique = true)
    private String channelId;

    @Column(name = "check_new_video", nullable = false)
    private boolean checkNewVideo;
}
