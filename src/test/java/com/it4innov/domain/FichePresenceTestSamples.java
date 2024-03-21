package com.it4innov.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FichePresenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FichePresence getFichePresenceSample1() {
        return new FichePresence().id(1L).libelle("libelle1").description("description1").codeEvenement(1L).codeTypeEvenement(1L);
    }

    public static FichePresence getFichePresenceSample2() {
        return new FichePresence().id(2L).libelle("libelle2").description("description2").codeEvenement(2L).codeTypeEvenement(2L);
    }

    public static FichePresence getFichePresenceRandomSampleGenerator() {
        return new FichePresence()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .codeEvenement(longCount.incrementAndGet())
            .codeTypeEvenement(longCount.incrementAndGet());
    }
}
