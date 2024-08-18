package io.sailex.twitchclipsnotifier.config;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import io.sailex.twitchclipsnotifier.rest.TwitchWebhookController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TwitchWebhookController webhookController() {
        return new TwitchWebhookController();
    }

    @Bean
    public TwitchClient twitchClient() {
        TwitchClient twitchClient = TwitchClientBuilder.builder().withEnableHelix(true).build();
        twitchClient.getClientHelper().enableClipEventListener("twitch4j");
        return twitchClient;
    }

}
