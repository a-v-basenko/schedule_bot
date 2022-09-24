package khai.basenko.schedule_bot.service;

import khai.basenko.schedule_bot.config.BotConfig;
import khai.basenko.schedule_bot.model.User;
import khai.basenko.schedule_bot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;

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

        // User registration when they're starting the bot
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Chat chat = update.getMessage().getChat();

            if (messageText.equals("/start")) {
                registerUser(chat);
            }

            // echo (text only)
            sendMessage(chat, messageText);
        }
    }

    /**
     * Sends a text message
     *
     * @param chat        chat to which you want to send a message
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

    /**
     * Adds a user to the database
     *
     * @param chat chat with the user which you want to add to the database
     */
    private void registerUser(Chat chat) {
        // There is no need to add a user who is already added to the database
        if (userRepository.existsById(chat.getId())) return;

        User user = new User();

        user.setChatId(chat.getId());
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        // Adding the user to the database
        userRepository.save(user);
        log.info("User saved: " + user);
    }
}
