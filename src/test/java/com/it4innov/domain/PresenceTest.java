package com.it4innov.domain;

import static com.it4innov.domain.FichePresenceTestSamples.*;
import static com.it4innov.domain.PresenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presence.class);
        Presence presence1 = getPresenceSample1();
        Presence presence2 = new Presence();
        assertThat(presence1).isNotEqualTo(presence2);

        presence2.setId(presence1.getId());
        assertThat(presence1).isEqualTo(presence2);

        presence2 = getPresenceSample2();
        assertThat(presence1).isNotEqualTo(presence2);
    }

    @Test
    void fichePresenceTest() throws Exception {
        Presence presence = getPresenceRandomSampleGenerator();
        FichePresence fichePresenceBack = getFichePresenceRandomSampleGenerator();

        presence.setFichePresence(fichePresenceBack);
        assertThat(presence.getFichePresence()).isEqualTo(fichePresenceBack);

        presence.fichePresence(null);
        assertThat(presence.getFichePresence()).isNull();
    }
}
