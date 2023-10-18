package ru.verpul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.verpul.component.HomeThingsBot;

@Configuration
public class HomeThingsBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(HomeThingsBot homeThingsBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(homeThingsBot);
        return telegramBotsApi;
    }

}
