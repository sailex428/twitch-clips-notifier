package io.sailex.twitchclipsnotifier.config;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import io.sailex.twitchclipsnotifier.clips.TwitchClipsHandler;
import io.sailex.twitchclipsnotifier.events.TwitchEventsHandler;
import io.sailex.twitchclipsnotifier.notification.NotificationBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class AppConfiguration {

    @Bean
    public Logger LOGGER() {
        return LoggerFactory.getLogger("twitch-clips-notifier");
    }

    @Bean
    public TwitchConfigProperties twitchConfigProperties() {
        return new TwitchConfigProperties();
    }

    @Bean
    public TelegramConfigProperties telegramConfigProperties() {
        return new TelegramConfigProperties();
    }

    @Bean
    public TwitchClient twitchClient(TwitchConfigProperties twitchConfigProperties) {
        return TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withClientId(twitchConfigProperties.getClientId())
                .withClientSecret(twitchConfigProperties.getClientSecret())
                .build();
    }

    @Bean
    public NotificationBot notificationBot(
            TelegramConfigProperties telegramConfigProperties, Logger LOGGER) {
        return new NotificationBot(telegramConfigProperties, LOGGER);
    }

    @Bean
    public TelegramBotsLongPollingApplication botsApplication(
            NotificationBot notificationBot,
            TelegramConfigProperties telegramConfigProperties,
            Logger LOGGER) {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        try {
            botsApplication.registerBot(telegramConfigProperties.getBotToken(), notificationBot);
            LOGGER.info("Registered bot successfully");
        } catch (TelegramApiException e) {
            LOGGER.error("error occurred while registering notification bot.", e);
        }
        return botsApplication;
    }

    @Bean
    public TwitchClipsHandler twitchClipsHandler(
            TwitchClient twitchClient,
            TwitchConfigProperties twitchConfigProperties,
            NotificationBot notificationBot,
            Logger LOGGER) {
        return new TwitchClipsHandler(twitchClient, twitchConfigProperties, notificationBot, LOGGER);
    }

    @Bean
    public TwitchEventsHandler twitchEventHandler(
            TwitchClient twitchClient,
            TwitchConfigProperties twitchConfigProperties,
            TwitchClipsHandler twitchClipsHandler) {
        TwitchEventsHandler twitchEventsHandler =
                new TwitchEventsHandler(twitchClient, twitchConfigProperties, twitchClipsHandler);
        twitchEventsHandler.registerEvents();
        return twitchEventsHandler;
    }
}
