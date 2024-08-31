package io.sailex.twitchclipsnotifier.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twitch")
public class TwitchConfigProperties {

    private List<String> channelIds;

    private String clientId;

    private String clientSecret;

    private int delayClipEvaluation;

    private int minViewCount;

    public int getMinViewCount() {
        return minViewCount;
    }

    public void setMinViewCount(int minViewCount) {
        this.minViewCount = minViewCount;
    }

    public int getDelayClipEvaluation() {
        return delayClipEvaluation;
    }

    public void setDelayClipEvaluation(int delayClipEvaluation) {
        this.delayClipEvaluation = delayClipEvaluation;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setChannelIds(List<String> channelIds) {
        this.channelIds = channelIds;
    }

    public List<String> getChannelIds() {
        return channelIds;
    }

    public String getClientId() {
        return clientId;
    }
}
