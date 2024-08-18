package io.sailex.twitchclipsnotifier.clips;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Clip;
import io.sailex.twitchclipsnotifier.config.TwitchConfigProperties;
import io.sailex.twitchclipsnotifier.notification.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TwitchClipsHandler {

    private final TwitchClient twitchClient;

    private final TwitchConfigProperties twitchConfigProperties;

    private final List<Clip> currentClips = new ArrayList<>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final NotificationBot notificationBot;

    @Autowired
    public TwitchClipsHandler(TwitchClient twitchClient, TwitchConfigProperties twitchConfigProperties, NotificationBot notificationBot) {
        this.twitchClient = twitchClient;
        this.twitchConfigProperties = twitchConfigProperties;
        this.notificationBot = notificationBot;
    }

    public void analyzeClip() {
        String clipId = currentClips.getLast().getId();
        executor.schedule(() ->
                calculateRelevance(clipId),
                twitchConfigProperties.getDelayClipEvaluation(),
                TimeUnit.MINUTES
        );
    }

    private void calculateRelevance(String clipId) {
        Clip clip = getClipOfId(clipId);
        if (clip.getViewCount() > 0) {
            notificationBot.sendClipNotification(clip.getCreatorName(), clip.getTitle(), clip.getUrl(), clip.getVodOffset());
        }
    }

    private Clip getClipOfId(String clipId) {
        return twitchClient.getHelix().getClips(
                null, null, null,
                Collections.singletonList(clipId), null, null, null,
                null, null, null).execute().getData().getFirst();
    }

    public List<Clip> getCurrentClips() {
        return currentClips;
    }

}
