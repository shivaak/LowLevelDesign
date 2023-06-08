package ratelimiter.tokenbucket;

import ratelimiter.RateLimiter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TokenBucketRateLimiter implements RateLimiter {

    private int bucketCapacity;
    private int tokensPerInterval;
    private Duration interval;
    private int currentCapacity;


    private final Map<String, UserTokens> userTokenMap;

    TokenBucketRateLimiter(int bucketCapacity, int tokensPerInterval, Duration interval){
        this.bucketCapacity = bucketCapacity;
        this.tokensPerInterval = tokensPerInterval;
        this.interval = interval;
        this.currentCapacity=bucketCapacity;
        this.userTokenMap = new HashMap<>();
    }


    public synchronized boolean allowRequest(String userId) {

        if(!userTokenMap.containsKey(userId)){
            userTokenMap.put(userId, new UserTokens(this.bucketCapacity, this.tokensPerInterval, this.interval));
        }

        UserTokens userTokens = userTokenMap.get(userId);
        userTokens.refill();

        //System.out.println(currentCapacity);
        if(userTokens.getCurrentCapacity()>0) {
            userTokens.reduceOneToken();
            return true;
        }
        return false;
    }
}
