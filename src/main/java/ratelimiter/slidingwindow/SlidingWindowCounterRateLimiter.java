package ratelimiter.slidingwindow;

import ratelimiter.RateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowCounterRateLimiter implements RateLimiter {
    private final int requests;
    private final int windowTimeInSec;
    private final int bucketSize;
    private final Map<String, RequestCounters> ratelimiterMap;
    private final Lock lock;

    public SlidingWindowCounterRateLimiter(int requests, int windowTimeInSec, int bucketSize) {
        this.requests = requests;
        this.windowTimeInSec = windowTimeInSec;
        this.bucketSize = bucketSize;
        this.ratelimiterMap = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public void addUser(String userId) {
        lock.lock();
        try {
            if (ratelimiterMap.containsKey(userId)) {
                throw new RuntimeException("User already present");
            }
            ratelimiterMap.put(userId, new RequestCounters(requests, windowTimeInSec, bucketSize));
        } finally {
            lock.unlock();
        }
    }

    public void removeUser(String userId) {
        lock.lock();
        try {
            ratelimiterMap.remove(userId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean allowRequest(String userId) {
        lock.lock();
        try {
            RequestCounters userCounters = ratelimiterMap.get(userId);
            if (userCounters == null) {
                throw new RuntimeException("User is not present");
            }
            long currentTimestamp = System.currentTimeMillis() / 1000;
            userCounters.evictOlderBuckets(currentTimestamp);
            long currentBucket = userCounters.getBucket(currentTimestamp);
            int bucketCount = userCounters.getCounts().getOrDefault(currentBucket, 0);
            userCounters.getCounts().put(currentBucket, bucketCount + 1);
            userCounters.setTotalCounts(userCounters.getTotalCounts() + 1);
            return userCounters.getTotalCounts() <= requests;
        } finally {
            lock.unlock();
        }
    }

    private static class RequestCounters {
        private final Map<Long, Integer> counts;
        private int totalCounts;
        private final int requests;
        private final int windowTimeInSec;
        private final int bucketSize;

        public RequestCounters(int requests, int windowTimeInSec, int bucketSize) {
            this.counts = new HashMap<>();
            this.totalCounts = 0;
            this.requests = requests;
            this.windowTimeInSec = windowTimeInSec;
            this.bucketSize = bucketSize;
        }

        public long getBucket(long timestamp) {
            long factor = windowTimeInSec / bucketSize;
            return (timestamp / factor) * factor;
        }

        public void evictOlderBuckets(long currentTimestamp) {
            long oldestValidBucket = getBucket(currentTimestamp - windowTimeInSec);
            counts.keySet().removeIf(bucket -> bucket < oldestValidBucket);
        }

        public Map<Long, Integer> getCounts() {
            return counts;
        }

        public int getTotalCounts() {
            return totalCounts;
        }

        public void setTotalCounts(int totalCounts) {
            this.totalCounts = totalCounts;
        }
    }
}
