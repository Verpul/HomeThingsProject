package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.TwitchToken;

public interface TwitchTokenRepository extends JpaRepository<TwitchToken, Long> {

    @Query(value = "SELECT * FROM twitch_token LIMIT 1", nativeQuery = true)
    TwitchToken getTwitchToken();
}
