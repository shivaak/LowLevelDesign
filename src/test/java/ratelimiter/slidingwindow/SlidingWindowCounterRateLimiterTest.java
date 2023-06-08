package ratelimiter.slidingwindow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowCounterRateLimiterTest {
    private SlidingWindowCounterRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new SlidingWindowCounterRateLimiter(3, 5, 3);
    }

    @Test
    void addUser_ValidUser_Success() {
        rateLimiter.addUser("user1");
        assertTrue(rateLimiter.allowRequest("user1"));
    }

    @Test
    void addUser_DuplicateUser_ExceptionThrown() {
        rateLimiter.addUser("user1");
        assertThrows(RuntimeException.class, () ->
                rateLimiter.addUser("user1")
        );
    }

    @Test
    void removeUser_ValidUser_Success() {
        rateLimiter.addUser("user1");
        rateLimiter.removeUser("user1");
        assertThrows(RuntimeException.class, () ->
                rateLimiter.allowRequest("user1")
        );
    }

    @Test
    void shouldAllowServiceCall_UserNotPresent_ExceptionThrown() {
        assertThrows(RuntimeException.class, () ->
                rateLimiter.allowRequest("user1")
        );
    }

    @Test
    void shouldAllowServiceCall_RateLimitNotExceeded_True() {
        rateLimiter.addUser("user1");
        assertTrue(rateLimiter.allowRequest("user1"));
        assertTrue(rateLimiter.allowRequest("user1"));
        assertTrue(rateLimiter.allowRequest("user1"));
    }

    @Test
    void shouldAllowServiceCall_RateLimitExceeded_False() {
        rateLimiter.addUser("user1");
        assertTrue(rateLimiter.allowRequest("user1"));
        assertTrue(rateLimiter.allowRequest("user1"));
        assertTrue(rateLimiter.allowRequest("user1"));
        assertFalse(rateLimiter.allowRequest("user1"));
    }
}