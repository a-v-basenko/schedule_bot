package khai.basenko.schedule_bot.service;

import khai.basenko.schedule_bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            // echo (only text)
            Chat chat = update.getMessage().getChat();
            String messageText = update.getMessage().getText();
            sendMessage(chat, messageText);
        }
    }

    /**
     * Sends a text message
     * @param chat chat to which you want to send a message
     * @param messageText text of the message
     */
    private void sendMessage(Chat chat, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(messageText);

        try {
            execute(message);
            log.info("Message to " + chat + " with text: " + messageText);
        } catch (TelegramApiException e) {
            log.error("Error occurred " + e.getMessage());
        }
    }
}
