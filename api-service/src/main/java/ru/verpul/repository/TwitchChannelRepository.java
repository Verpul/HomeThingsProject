package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.TwitchChannel;

import java.util.Optional;

public interface TwitchChannelRepository extends JpaRepository<TwitchChannel, Long> {

    @Query("SELECT t FROM TwitchChannel t WHERE t.channelName = :channelName")
    Optional<TwitchChannel> findByChannelName(String channelName);
}
