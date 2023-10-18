package ru.verpul.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.verpul.enums.Command;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.message.ReminderMessage;

import java.util.Locale;

@Component
@Slf4j
public class HomeThingsBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.chat-id}")
    private Long chatId;

    private static final String HELP_COMMAND = "/help";
    private static final String REMINDER_COMMAND = "дела";

    private static final String UNRECOGNIZED_COMMAND_MESSAGE = "Команда не распознана";

    private final LocalServiceFeign localServiceFeign;
    private final ReminderMessage reminderMessage;

    public HomeThingsBot(@Value("${bot.token}") String botToken, LocalServiceFeign localServiceFeign, ReminderMessage reminderMessage) {
        super(botToken);
        this.localServiceFeign = localServiceFeign;
        this.reminderMessage = reminderMessage;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getChatId().equals(chatId)) {
            Message receivedMessage = update.getMessage();
            handleMessage(receivedMessage);
        }
    }

    private void handleMessage(Message message) {
        String command = message.getText().toLowerCase(Locale.ROOT);

        switch (command) {
            case HELP_COMMAND:
                sendMessage(Command.HELP.getText());
                break;
            case REMINDER_COMMAND:
                reminderMessage.getRemindersMessage(localServiceFeign.getRemindersWithProperRemindDate());
                break;
            default:
                sendMessage(UNRECOGNIZED_COMMAND_MESSAGE);
                break;
        }
    }

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage(this.chatId.toString(), message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: " + e.getMessage(), e);
        }
    }

}
