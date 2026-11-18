package model;

import enums.TierType;

import java.time.Duration;

public class Tier {
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TierType getTier() {
        return tier;
    }

    public void setTier(TierType tier) {
        this.tier = tier;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public Duration getWindowDuration() {
        return windowDuration;
    }

    public void setWindowDuration(Duration windowDuration) {
        this.windowDuration = windowDuration;
    }

    TierType tier;
    int maxRequests;
    Duration windowDuration;
}
