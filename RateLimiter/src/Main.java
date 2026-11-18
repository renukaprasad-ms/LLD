import enums.TierType;
import model.Tier;
import model.User;
import repository.InMemoryUserRepository;
import service.AlignedFixedWindowRateLimiter;
import service.RateLimiter;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final  InMemoryUserRepository userRepo = new InMemoryUserRepository() ;
    private static final RateLimiter rateLimiter = new AlignedFixedWindowRateLimiter(userRepo);

    public static void main(String[] args) throws InterruptedException {
        addInitialData();
        testConcurrentRequests();

//        for(int i = 0; i < 20; i++) {
//            boolean allowed = rateLimiter.allowRequest(4);
//            System.out.println("request" + i + allowed);
//        }

    }

    public static void addInitialData() {
        long id = 1;
        Tier t1 = new Tier();
        t1.setId(id++);
        t1.setTier(TierType.FREE);
        t1.setMaxRequests(20);
        t1.setWindowDuration(Duration.ofSeconds(10));

        Tier t2 = new Tier();
        t2.setId(id++);
        t2.setTier(TierType.PREMIUM);
        t2.setMaxRequests(60);
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

    public static void testConcurrentRequests() throws InterruptedException {

        int totalRequests = 100;

        ExecutorService executor = Executors.newFixedThreadPool(20);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalRequests);

        AtomicInteger allowedCount = new AtomicInteger(0);
        AtomicInteger rejectedCount = new AtomicInteger(0);

        RateLimiter limiter = new AlignedFixedWindowRateLimiter(userRepo);

        try {
            for (int i = 0; i < totalRequests; i++) {

                executor.submit(() -> {
                    try {
                        // Wait until all workers are ready to start
                        startLatch.await();

                        boolean allowed = limiter.allowRequest(3L);

                        if (allowed) {
                            allowedCount.incrementAndGet();
                        } else {
                            rejectedCount.incrementAndGet();
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        // Mark this task as completed
                        doneLatch.countDown();
                    }
                });
            }

            // Release the workers together
            startLatch.countDown();

            // Wait until all 100 tasks have completed
            doneLatch.await();

            System.out.println("Total Requests: " + totalRequests);
            System.out.println("Allowed: " + allowedCount.get());
            System.out.println("Rejected: " + rejectedCount.get());

        } finally {
            executor.shutdown();
        }
    }
}

