package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FichePresenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FichePresenceDTO.class);
        FichePresenceDTO fichePresenceDTO1 = new FichePresenceDTO();
        fichePresenceDTO1.setId(1L);
        FichePresenceDTO fichePresenceDTO2 = new FichePresenceDTO();
        assertThat(fichePresenceDTO1).isNotEqualTo(fichePresenceDTO2);
        fichePresenceDTO2.setId(fichePresenceDTO1.getId());
        assertThat(fichePresenceDTO1).isEqualTo(fichePresenceDTO2);
        fichePresenceDTO2.setId(2L);
        assertThat(fichePresenceDTO1).isNotEqualTo(fichePresenceDTO2);
        fichePresenceDTO1.setId(null);
        assertThat(fichePresenceDTO1).isNotEqualTo(fichePresenceDTO2);
    }
}
