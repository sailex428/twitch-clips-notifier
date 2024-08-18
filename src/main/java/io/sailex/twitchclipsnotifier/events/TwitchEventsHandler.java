package io.sailex.twitchclipsnotifier.events;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelClipCreatedEvent;
import io.sailex.twitchclipsnotifier.config.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitchEventsHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(TwitchEventsHandler.class);
    private final TwitchClient twitchClient;
    private final ConfigProperties configProperties;

    @Autowired
    public TwitchEventsHandler(TwitchClient twitchClient, ConfigProperties configProperties) {
        this.twitchClient = twitchClient;
        this.configProperties = configProperties;
    }

    public void registerEvents() {
        enableEventListener();
        handleEvents();
    }

    private void enableEventListener() {
        twitchClient.getClientHelper().enableClipEventListener(configProperties.getChannelIds());
    }

    private void handleEvents() {
        twitchClient.getEventManager().onEvent(ChannelClipCreatedEvent.class, event ->
                LOGGER.info("[{}] | [{}]", event.getChannel().getName(), event.getClip().getUrl())
        );
    }

}
