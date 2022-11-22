package io.proj3ct.vkbotspring.service;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.users.Get;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.basic.User;
import com.vdurmont.emoji.EmojiParser;
import io.proj3ct.vkbotspring.config.BotConfig;
import io.proj3ct.vkbotspring.data.UserRepository;
import io.proj3ct.vkbotspring.module.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
public class SimpleVkBot extends LongPollBot {
    public final String HELP_TEXT = "HELP_TEXT";
    BotConfig botConfig;
    UserRepository userRepository;

    @Autowired
    public SimpleVkBot(BotConfig botConfig,
                       UserRepository userRepository) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
    }

    @Override
    public String getAccessToken() {
        return botConfig.getBotToken();
    }


    @Override
    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        String messageText = message.getText();
        Integer peerId = message.getPeerId();
        Integer userId = message.getFromId();
        if (message.hasText()) {
            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(peerId, userId);
                    } catch (VkApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/help":
                    sendMessage(peerId, HELP_TEXT);
                    break;
                default:
                    someFunctionality(peerId);
            }
        }
    }

    private void startCommandReceived(Integer peerId, Integer userId) throws VkApiException {
        User user = getUser(userId);
        String userFirstName = user.getFirstName();
        registerUser(userId ,user);
        String answer = "Hello, " + userFirstName + ", nice to meet you! " + ":smile:";
        sendMessage(peerId, answer);
    }

    private void sendMessage(Integer peerId, String text) {
        String answer = EmojiParser.parseToUnicode(text);
        try {
            vk.messages.send()
                    .setPeerId(peerId)
                    .setMessage(answer)
                    .execute();
        } catch (VkApiException e) {
            log.error("Send message error occurred: " + e.getMessage());
        }
    }

    private void someFunctionality(Integer peerId) {
        String text = "Sorry, command was not recognised";
        sendMessage(peerId, text);
    }

    private User getUser(Integer userId) throws VkApiException {
        Get.ResponseBody responseBody = vk.users.get()
                .setUserIds(String.valueOf(userId))
                .execute();
        return responseBody.getResponse().get(0);
    }

    private void registerUser(Integer userId ,User user){
        if(!userRepository.findById(Long.valueOf(userId)).isPresent()){
            String userName = user.getFirstName() + " " + user.getLastName();
            UserModel userModel = new UserModel();
            userModel.setId(Long.valueOf(userId));
            userModel.setUsername(userName);
            userModel.setFirstName(user.getFirstName());
            userModel.setLastName(user.getLastName());
            userModel.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(userModel);
            log.info("New user was registered: " + userName);
        }
    }
}
