package io.proj3ct.vkbotspring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {

    @Value("${bot.id}")
    private String botID;

    @Value("${bot.token}")
    private String botToken;

}
