package com.it4innov.domain;

import static com.it4innov.domain.FichePresenceTestSamples.*;
import static com.it4innov.domain.PresenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FichePresenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FichePresence.class);
        FichePresence fichePresence1 = getFichePresenceSample1();
        FichePresence fichePresence2 = new FichePresence();
        assertThat(fichePresence1).isNotEqualTo(fichePresence2);

        fichePresence2.setId(fichePresence1.getId());
        assertThat(fichePresence1).isEqualTo(fichePresence2);

        fichePresence2 = getFichePresenceSample2();
        assertThat(fichePresence1).isNotEqualTo(fichePresence2);
    }

    @Test
    void presenceTest() throws Exception {
        FichePresence fichePresence = getFichePresenceRandomSampleGenerator();
        Presence presenceBack = getPresenceRandomSampleGenerator();

        fichePresence.addPresence(presenceBack);
        assertThat(fichePresence.getPresences()).containsOnly(presenceBack);
        assertThat(presenceBack.getFichePresence()).isEqualTo(fichePresence);

        fichePresence.removePresence(presenceBack);
        assertThat(fichePresence.getPresences()).doesNotContain(presenceBack);
        assertThat(presenceBack.getFichePresence()).isNull();

        fichePresence.presences(new HashSet<>(Set.of(presenceBack)));
        assertThat(fichePresence.getPresences()).containsOnly(presenceBack);
        assertThat(presenceBack.getFichePresence()).isEqualTo(fichePresence);

        fichePresence.setPresences(new HashSet<>());
        assertThat(fichePresence.getPresences()).doesNotContain(presenceBack);
        assertThat(presenceBack.getFichePresence()).isNull();
    }
}
