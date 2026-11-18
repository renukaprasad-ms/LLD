package service;

import model.RateLimitState;
import model.User;
import repository.InMemoryUserRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlignedFixedWindowRateLimiter implements RateLimiter{

    AlignedFixedWindowRateLimiter(InMemoryUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private InMemoryUserRepository userRepo;
    private ConcurrentHashMap<Long, RateLimitState> state = new ConcurrentHashMap<>();

    @Override
    public boolean allowRequest(long userId) {
        AtomicBoolean allowed = new AtomicBoolean();
        User u = userRepo.getUseraById(userId);
        long WindowDurationMillis = 10_000L;
        long currentTime = System.currentTimeMillis();
        long window = (currentTime / WindowDurationMillis) * WindowDurationMillis;
        state.compute(userId,(id,currentState) -> {
            if(currentState == null || currentState.getWindowStartTime() == window) {
                RateLimitState newState = new RateLimitState();
                newState.setWindowStartTime(window);
                newState.setRemainingRequests(u.getTier().getMaxRequests() - 1);
                allowed.set(true);
            }
            if(currentState.getRemainingRequests() <= 0) {
                return currentState;
            }

            currentState.setRemainingRequests(currentState.getRemainingRequests() - 1);
            allowed.set(true);
            return currentState;
        });
        return allowed.get();
    }
}
