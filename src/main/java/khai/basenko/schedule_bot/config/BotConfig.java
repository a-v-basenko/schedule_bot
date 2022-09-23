package khai.basenko.schedule_bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Stores the username and token the bot takes from the file application.properties.
 * The file application.properties.example contains an example of how
 * the file application.properties should be filled out.
 */
@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;
}
