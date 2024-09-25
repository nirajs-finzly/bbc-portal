package com.bbc.app.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private static final long TIME_LIMIT = TimeUnit.MINUTES.toMillis(1); // 1 minute
    private static final ConcurrentHashMap<String, Long> requestTimes = new ConcurrentHashMap<>();

    public static boolean isAllowed(String userId) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = requestTimes.get(userId);

        if (lastRequestTime == null || (currentTime - lastRequestTime) > TIME_LIMIT) {
            requestTimes.put(userId, currentTime);
            return true;
        }
        return false;
    }
}
