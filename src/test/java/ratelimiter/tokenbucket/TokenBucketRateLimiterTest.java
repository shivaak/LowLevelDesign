package ratelimiter.tokenbucket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {

    TokenBucketRateLimiter rateLimiter;

    @BeforeEach
    public void setup(){
        rateLimiter = new TokenBucketRateLimiter(10, 10, Duration.ofSeconds(6));
    }

    @Test
    public void testAllowRequest() {
        String userId = "user1";
        for (int i = 1; i <= 10; i++) {
            assertTrue(rateLimiter.allowRequest(userId));
        }
    }

    @Test
    public void testDenyRequest(){
        String userId = "user1";
        for(int i=1;i<=20;i++){
            if(i<=10)
                assertTrue(rateLimiter.allowRequest(userId));
            else
                assertFalse(rateLimiter.allowRequest(userId));
        }
    }

    @Test
    public void testRefillRequest() throws InterruptedException {
        String userId = "user1";
        for(int i=1;i<=20;i++){
            if(i<=10)
                assertTrue(rateLimiter.allowRequest(userId));
            else
                assertFalse(rateLimiter.allowRequest(userId));
        }
        Thread.sleep(6000);
        for(int i=1;i<=20;i++){
            if(i<=10)
                assertTrue(rateLimiter.allowRequest(userId));
            else
                assertFalse(rateLimiter.allowRequest(userId));
        }
    }

    @Test
    public void testParallelRequest() throws ExecutionException, InterruptedException {
        String userId = "user1";
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<Boolean> task = ()->{
            boolean isAllowed = rateLimiter.allowRequest(userId);
            System.out.println(String.format("Thread %s permission %s", Thread.currentThread().getName() , isAllowed));
            return isAllowed;
        };
        ArrayList<Future<Boolean>> futureArrayList = new ArrayList<>();
        for(int i=1;i<=20;i++){
            futureArrayList.add(executor.submit(task));
        }
        executor.shutdown();

        int allowedCount =0;
        for(Future<Boolean> f : futureArrayList){
            Boolean isAllowed = f.get();
            if(isAllowed)allowedCount++;
        }
        assertEquals(10, allowedCount );

    }

    @Test
    public void testMultipleUserRequest(){
        String userId1 = "user1";
        String userId2 = "user2";
        for(int i=1;i<=20;i++){
            if(i<=10) {
                assertTrue(rateLimiter.allowRequest(userId1));
                assertTrue(rateLimiter.allowRequest(userId2));
            } else{
                assertFalse(rateLimiter.allowRequest(userId1));
                assertFalse(rateLimiter.allowRequest(userId2));
            }
        }
    }


}
