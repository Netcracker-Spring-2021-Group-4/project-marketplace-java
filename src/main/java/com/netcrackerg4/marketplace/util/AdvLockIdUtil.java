package com.netcrackerg4.marketplace.util;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

public class AdvLockIdUtil {
    public static long toLong(int a, int b) {
        return (((long) a) << 32) & b;
    }

    public static long toLong(List<Long> xs) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * xs.size());
        for (Long x : xs) buffer.putLong(x);
        return UUID.nameUUIDFromBytes(buffer.array()).getMostSignificantBits();
    }
}
