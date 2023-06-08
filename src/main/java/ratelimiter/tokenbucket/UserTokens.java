package ratelimiter.tokenbucket;

import java.time.Duration;
import java.time.Instant;

public class UserTokens {
    private final int bucketCapacity;
    private int currentCapacity;
    private final int tokensPerInterval;
    private final Duration interval;

    private Instant lastRefillTimeStamp;

    public UserTokens(int bucketCapacity, int tokensPerInterval, Duration interval) {
        this.bucketCapacity = bucketCapacity;
        this.tokensPerInterval = tokensPerInterval;
        this.interval = interval;
        this.lastRefillTimeStamp = Instant.now();
        this.currentCapacity = bucketCapacity;
    }

    void refill() {
        Instant now  = Instant.now();
        Duration duration = Duration.between(lastRefillTimeStamp, now);
        int tokensToBeAdded = (int)(duration.getSeconds()/interval.getSeconds()) * tokensPerInterval;
        //System.out.println(String.format("Tokens to be added %d duration %d refill rate %d",tokensToBeAdded, (int)duration.getSeconds() , refillRate));
        currentCapacity= Math.min(currentCapacity+tokensToBeAdded, bucketCapacity);
        this.lastRefillTimeStamp = Instant.now();
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void reduceOneToken() {
        currentCapacity--;
    }
}
