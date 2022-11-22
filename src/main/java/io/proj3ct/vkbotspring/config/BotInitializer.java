package io.proj3ct.vkbotspring.config;

import api.longpoll.bots.exceptions.VkApiException;
import io.proj3ct.vkbotspring.service.SimpleVkBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BotInitializer {
    SimpleVkBot simpleVkBot;

    @Autowired
    public BotInitializer(SimpleVkBot simpleVkBot) {
        this.simpleVkBot = simpleVkBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            simpleVkBot.startPolling();
        } catch (VkApiException e) {
            log.error("Bootstrap error occurred: " + e.getMessage());
        }
    }
}
