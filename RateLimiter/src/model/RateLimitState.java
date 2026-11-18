package model;

public class RateLimitState {
    int remainingRequests;
    long WindowStartTime;

    public int getRemainingRequests() {
        return remainingRequests;
    }

    public void setRemainingRequests(int remainingRequests) {
        this.remainingRequests = remainingRequests;
    }

    public long getWindowStartTime() {
        return WindowStartTime;
    }

    public void setWindowStartTime(long windowStartTime) {
        WindowStartTime = windowStartTime;
    }
}
