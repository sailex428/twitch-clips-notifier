package io.sailex.twitchclipsnotifier.notification;

import io.sailex.twitchclipsnotifier.config.TelegramConfigProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class NotificationBot implements LongPollingSingleThreadUpdateConsumer {

    private final Logger LOGGER;
    private final TelegramConfigProperties config;
    private final TelegramClient telegramClient;

    @Autowired
    public NotificationBot(TelegramConfigProperties telegramConfigProperties, Logger LOGGER) {
        this.config = telegramConfigProperties;
        this.LOGGER = LOGGER;
        telegramClient = new OkHttpTelegramClient(telegramConfigProperties.getBotToken());
    }

    public void sendClipNotification(String channelName, String clipTitle, String clipUrl, int vodOffset) {
        SendMessage message = SendMessage
                .builder()
                .chatId(config.getChannel())
                .text(clipTitle + "\n" + channelName + "\n" + clipUrl + "\n[ " + vodOffset + " ]")
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("error sending message {} to client", message.getText(), e);
        }

    }

    @Override
    public void consume(Update update) {}
}
