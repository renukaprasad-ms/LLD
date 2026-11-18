package service;

public interface RateLimiter {
    public boolean allowRequest(long userId) ;
}
