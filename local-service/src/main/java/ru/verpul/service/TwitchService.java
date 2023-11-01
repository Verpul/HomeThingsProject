package ru.verpul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.model.TwitchToken;
import ru.verpul.repository.TwitchTokenRepository;

@Service
@RequiredArgsConstructor
public class TwitchService {

    private final TwitchTokenRepository twitchTokenRepository;

    public TwitchToken getTwitchToken() {
        return twitchTokenRepository.getTwitchToken();
    }

    @Transactional
    public void saveToken(TwitchToken twitchToken) {
        TwitchToken token = twitchTokenRepository.getTwitchToken();

        if (token != null) {
            twitchToken.setId(token.getId());
        }

        twitchTokenRepository.save(twitchToken);
    }
}
