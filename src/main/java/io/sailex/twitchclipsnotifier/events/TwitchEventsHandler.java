package io.sailex.twitchclipsnotifier.events;

import com.github.twitch4j.TwitchClient;
import io.sailex.twitchclipsnotifier.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitchEventHandler {

    private final TwitchClient twitchClient;
    private final ConfigProperties configProperties;

    @Autowired
    public TwitchEventHandler(TwitchClient twitchClient, ConfigProperties configProperties) {
        this.twitchClient = twitchClient;
        this.configProperties = configProperties;
    }

    public void registerEvents() {
        twitchClient.getClientHelper().enableClipEventListener(configProperties.getChannelIds());
    }


}
