package io.sailex.twitchclipsnotifier.config;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import io.sailex.twitchclipsnotifier.events.TwitchEventsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ConfigProperties configProperties() {
        return new ConfigProperties();
    }

    @Bean
    public TwitchClient twitchClient(ConfigProperties configProperties) {
        return TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withClientId(configProperties.getClientId())
                .withClientSecret(configProperties.getClientSecret())
                .build();
    }

    @Bean
    public TwitchEventsHandler twitchEventHandler(TwitchClient twitchClient, ConfigProperties configProperties) {
        TwitchEventsHandler twitchEventsHandler = new TwitchEventsHandler(twitchClient, configProperties);
        twitchEventsHandler.registerEvents();
        return twitchEventsHandler;
    }

}
