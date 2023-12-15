package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.YoutubeChannel;

import java.util.Optional;

public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {

    @Query("SELECT y FROM YoutubeChannel y WHERE y.channelId = :channelId")
    Optional<YoutubeChannel> findByChannelId(String channelId);

}
