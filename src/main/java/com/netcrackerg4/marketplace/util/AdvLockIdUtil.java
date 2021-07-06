package com.netcrackerg4.marketplace.util;

public class AdvLockIdUtil {
    public static long toLong(int a, int b) {
        return (((long) a) << 32) & b;
    }
}
