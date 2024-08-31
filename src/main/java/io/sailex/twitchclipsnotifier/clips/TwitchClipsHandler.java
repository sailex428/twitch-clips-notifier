package io.sailex.twitchclipsnotifier.clips;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Clip;
import io.sailex.twitchclipsnotifier.config.TwitchConfigProperties;
import io.sailex.twitchclipsnotifier.notification.NotificationBot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitchClipsHandler {

    private final TwitchClient twitchClient;

    private final TwitchConfigProperties twitchConfigProperties;

    private final List<Clip> currentClips = new ArrayList<>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(20);

    private final NotificationBot notificationBot;

    private final Logger LOGGER;

    @Autowired
    public TwitchClipsHandler(
            TwitchClient twitchClient,
            TwitchConfigProperties twitchConfigProperties,
            NotificationBot notificationBot,
            Logger LOGGER) {
        this.twitchClient = twitchClient;
        this.twitchConfigProperties = twitchConfigProperties;
        this.notificationBot = notificationBot;
        this.LOGGER = LOGGER;
    }

    public void analyzeClip(String clipId) {
        executor.schedule(
                () -> calculateRelevance(clipId),
                twitchConfigProperties.getDelayClipEvaluation(),
                TimeUnit.MINUTES);
    }

    private void calculateRelevance(String clipId) {
        Clip clip = getClipOfId(clipId);
        if (clip.getViewCount() >= twitchConfigProperties.getMinViewCount()) {
            notificationBot.sendClipNotification(
                    clip.getBroadcasterName(),
                    clip.getTitle(),
                    clip.getUrl(),
                    clip.getVodOffset() == null ? 0 : clip.getVodOffset());
        }
    }

    private Clip getClipOfId(String clipId) {
        try {
            return twitchClient
                    .getHelix()
                    .getClips(
                            null,
                            null,
                            null,
                            Collections.singletonList(clipId),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null)
                    .queue()
                    .get()
                    .getData()
                    .getLast();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("failed to get clip {}", clipId, e);
            return new Clip();
        }
    }

    public List<Clip> getCurrentClips() {
        return currentClips;
    }
}
