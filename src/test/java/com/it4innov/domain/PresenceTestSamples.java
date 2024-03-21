package com.it4innov.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PresenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Presence getPresenceSample1() {
        return new Presence().id(1L).matriculeAdherant("matriculeAdherant1");
    }

    public static Presence getPresenceSample2() {
        return new Presence().id(2L).matriculeAdherant("matriculeAdherant2");
    }

    public static Presence getPresenceRandomSampleGenerator() {
        return new Presence().id(longCount.incrementAndGet()).matriculeAdherant(UUID.randomUUID().toString());
    }
}
