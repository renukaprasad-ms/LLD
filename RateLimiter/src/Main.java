import enums.TierType;
import model.Tier;
import model.User;
import repository.InMemoryUserRepository;
import service.AlignedFixedWindowRateLimiter;
import service.RateLimiter;

import java.time.Duration;

public class Main {

    private static final  InMemoryUserRepository userRepo = new InMemoryUserRepository() ;
    private static final RateLimiter rateLimiter = new AlignedFixedWindowRateLimiter(userRepo);

    public static void main(String[] args) {
        addInitialData();

        for(int i = 0; i < 20; i++) {
            boolean allowed = rateLimiter.allowRequest(1);
            System.out.println("request" + i + allowed);
        }

    }

    public static void addInitialData() {
        long id = 1;
        Tier t1 = new Tier();
        t1.setId(id++);
        t1.setTier(TierType.FREE);
        t1.setMaxRequests(10);
        t1.setWindowDuration(Duration.ofSeconds(10));

        Tier t2 = new Tier();
        t2.setId(id++);
        t2.setTier(TierType.PREMIUM);
        t2.setMaxRequests(20);
        t2.setWindowDuration(Duration.ofSeconds(10));

        User u1 = new User();
        u1.setId(id++);
        u1.setTier(t1);

        User u2 = new User();
        u2.setId(id++);
        u2.setTier(t2);

        userRepo.addUser(u1);
        userRepo.addUser(u2);
    }
}