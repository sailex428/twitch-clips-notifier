package io.sailex.twitchclipsnotifier.events;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelClipCreatedEvent;
import com.github.twitch4j.helix.domain.Clip;
import io.sailex.twitchclipsnotifier.clips.TwitchClipsHandler;
import io.sailex.twitchclipsnotifier.config.TwitchConfigProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitchEventsHandler {

    private final Logger LOGGER;

    private final TwitchClient twitchClient;

    private final TwitchConfigProperties twitchConfigProperties;

    private final TwitchClipsHandler twitchClipsHandler;

    @Autowired
    public TwitchEventsHandler(TwitchClient twitchClient, TwitchConfigProperties twitchConfigProperties,
                               TwitchClipsHandler twitchClipsHandler, Logger LOGGER) {
        this.twitchClient = twitchClient;
        this.twitchConfigProperties = twitchConfigProperties;
        this.twitchClipsHandler = twitchClipsHandler;
        this.LOGGER = LOGGER;
    }

    public void registerEvents() {
        enableEventListener();
        handleEvents();
    }

    private void enableEventListener() {
        twitchClient.getClientHelper().enableClipEventListener(twitchConfigProperties.getChannelIds());
    }

    private void handleEvents() {
        twitchClient.getEventManager().onEvent(ChannelClipCreatedEvent.class, event -> {
            Clip clip = event.getClip();
            LOGGER.info("new clip [{}] | [{}]", event.getChannel().getName(), clip.getUrl());
            this.twitchClipsHandler.getCurrentClips().add(clip);
            this.twitchClipsHandler.analyzeClip(clip.getId());
        });
    }

}
