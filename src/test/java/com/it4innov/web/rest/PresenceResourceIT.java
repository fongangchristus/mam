package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.FichePresence;
import com.it4innov.domain.Presence;
import com.it4innov.domain.enumeration.StatutPresence;
import com.it4innov.repository.PresenceRepository;
import com.it4innov.service.dto.PresenceDTO;
import com.it4innov.service.mapper.PresenceMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PresenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresenceResourceIT {

    private static final String DEFAULT_MATRICULE_ADHERANT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ADHERANT = "BBBBBBBBBB";

    private static final StatutPresence DEFAULT_STATUT_PRESENCE = StatutPresence.PRESENT;
    private static final StatutPresence UPDATED_STATUT_PRESENCE = StatutPresence.RETARD;

    private static final String ENTITY_API_URL = "/api/presences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private PresenceMapper presenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresenceMockMvc;

    private Presence presence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presence createEntity(EntityManager em) {
        Presence presence = new Presence().matriculeAdherant(DEFAULT_MATRICULE_ADHERANT).statutPresence(DEFAULT_STATUT_PRESENCE);
        return presence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presence createUpdatedEntity(EntityManager em) {
        Presence presence = new Presence().matriculeAdherant(UPDATED_MATRICULE_ADHERANT).statutPresence(UPDATED_STATUT_PRESENCE);
        return presence;
    }

    @BeforeEach
    public void initTest() {
        presence = createEntity(em);
    }

    @Test
    @Transactional
    void createPresence() throws Exception {
        int databaseSizeBeforeCreate = presenceRepository.findAll().size();
        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);
        restPresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeCreate + 1);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.getMatriculeAdherant()).isEqualTo(DEFAULT_MATRICULE_ADHERANT);
        assertThat(testPresence.getStatutPresence()).isEqualTo(DEFAULT_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void createPresenceWithExistingId() throws Exception {
        // Create the Presence with an existing ID
        presence.setId(1L);
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        int databaseSizeBeforeCreate = presenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeAdherantIsRequired() throws Exception {
        int databaseSizeBeforeTest = presenceRepository.findAll().size();
        // set the field null
        presence.setMatriculeAdherant(null);

        // Create the Presence, which fails.
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        restPresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isBadRequest());

        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutPresenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = presenceRepository.findAll().size();
        // set the field null
        presence.setStatutPresence(null);

        // Create the Presence, which fails.
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        restPresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isBadRequest());

        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPresences() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presence.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherant").value(hasItem(DEFAULT_MATRICULE_ADHERANT)))
            .andExpect(jsonPath("$.[*].statutPresence").value(hasItem(DEFAULT_STATUT_PRESENCE.toString())));
    }

    @Test
    @Transactional
    void getPresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get the presence
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL_ID, presence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presence.getId().intValue()))
            .andExpect(jsonPath("$.matriculeAdherant").value(DEFAULT_MATRICULE_ADHERANT))
            .andExpect(jsonPath("$.statutPresence").value(DEFAULT_STATUT_PRESENCE.toString()));
    }

    @Test
    @Transactional
    void getPresencesByIdFiltering() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        Long id = presence.getId();

        defaultPresenceShouldBeFound("id.equals=" + id);
        defaultPresenceShouldNotBeFound("id.notEquals=" + id);

        defaultPresenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPresenceShouldNotBeFound("id.greaterThan=" + id);

        defaultPresenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPresenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPresencesByMatriculeAdherantIsEqualToSomething() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where matriculeAdherant equals to DEFAULT_MATRICULE_ADHERANT
        defaultPresenceShouldBeFound("matriculeAdherant.equals=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the presenceList where matriculeAdherant equals to UPDATED_MATRICULE_ADHERANT
        defaultPresenceShouldNotBeFound("matriculeAdherant.equals=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllPresencesByMatriculeAdherantIsInShouldWork() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where matriculeAdherant in DEFAULT_MATRICULE_ADHERANT or UPDATED_MATRICULE_ADHERANT
        defaultPresenceShouldBeFound("matriculeAdherant.in=" + DEFAULT_MATRICULE_ADHERANT + "," + UPDATED_MATRICULE_ADHERANT);

        // Get all the presenceList where matriculeAdherant equals to UPDATED_MATRICULE_ADHERANT
        defaultPresenceShouldNotBeFound("matriculeAdherant.in=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllPresencesByMatriculeAdherantIsNullOrNotNull() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where matriculeAdherant is not null
        defaultPresenceShouldBeFound("matriculeAdherant.specified=true");

        // Get all the presenceList where matriculeAdherant is null
        defaultPresenceShouldNotBeFound("matriculeAdherant.specified=false");
    }

    @Test
    @Transactional
    void getAllPresencesByMatriculeAdherantContainsSomething() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where matriculeAdherant contains DEFAULT_MATRICULE_ADHERANT
        defaultPresenceShouldBeFound("matriculeAdherant.contains=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the presenceList where matriculeAdherant contains UPDATED_MATRICULE_ADHERANT
        defaultPresenceShouldNotBeFound("matriculeAdherant.contains=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllPresencesByMatriculeAdherantNotContainsSomething() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where matriculeAdherant does not contain DEFAULT_MATRICULE_ADHERANT
        defaultPresenceShouldNotBeFound("matriculeAdherant.doesNotContain=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the presenceList where matriculeAdherant does not contain UPDATED_MATRICULE_ADHERANT
        defaultPresenceShouldBeFound("matriculeAdherant.doesNotContain=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllPresencesByStatutPresenceIsEqualToSomething() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where statutPresence equals to DEFAULT_STATUT_PRESENCE
        defaultPresenceShouldBeFound("statutPresence.equals=" + DEFAULT_STATUT_PRESENCE);

        // Get all the presenceList where statutPresence equals to UPDATED_STATUT_PRESENCE
        defaultPresenceShouldNotBeFound("statutPresence.equals=" + UPDATED_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void getAllPresencesByStatutPresenceIsInShouldWork() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where statutPresence in DEFAULT_STATUT_PRESENCE or UPDATED_STATUT_PRESENCE
        defaultPresenceShouldBeFound("statutPresence.in=" + DEFAULT_STATUT_PRESENCE + "," + UPDATED_STATUT_PRESENCE);

        // Get all the presenceList where statutPresence equals to UPDATED_STATUT_PRESENCE
        defaultPresenceShouldNotBeFound("statutPresence.in=" + UPDATED_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void getAllPresencesByStatutPresenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList where statutPresence is not null
        defaultPresenceShouldBeFound("statutPresence.specified=true");

        // Get all the presenceList where statutPresence is null
        defaultPresenceShouldNotBeFound("statutPresence.specified=false");
    }

    @Test
    @Transactional
    void getAllPresencesByFichePresenceIsEqualToSomething() throws Exception {
        FichePresence fichePresence;
        if (TestUtil.findAll(em, FichePresence.class).isEmpty()) {
            presenceRepository.saveAndFlush(presence);
            fichePresence = FichePresenceResourceIT.createEntity(em);
        } else {
            fichePresence = TestUtil.findAll(em, FichePresence.class).get(0);
        }
        em.persist(fichePresence);
        em.flush();
        presence.setFichePresence(fichePresence);
        presenceRepository.saveAndFlush(presence);
        Long fichePresenceId = fichePresence.getId();
        // Get all the presenceList where fichePresence equals to fichePresenceId
        defaultPresenceShouldBeFound("fichePresenceId.equals=" + fichePresenceId);

        // Get all the presenceList where fichePresence equals to (fichePresenceId + 1)
        defaultPresenceShouldNotBeFound("fichePresenceId.equals=" + (fichePresenceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPresenceShouldBeFound(String filter) throws Exception {
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presence.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherant").value(hasItem(DEFAULT_MATRICULE_ADHERANT)))
            .andExpect(jsonPath("$.[*].statutPresence").value(hasItem(DEFAULT_STATUT_PRESENCE.toString())));

        // Check, that the count call also returns 1
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPresenceShouldNotBeFound(String filter) throws Exception {
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPresence() throws Exception {
        // Get the presence
        restPresenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();

        // Update the presence
        Presence updatedPresence = presenceRepository.findById(presence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPresence are not directly saved in db
        em.detach(updatedPresence);
        updatedPresence.matriculeAdherant(UPDATED_MATRICULE_ADHERANT).statutPresence(UPDATED_STATUT_PRESENCE);
        PresenceDTO presenceDTO = presenceMapper.toDto(updatedPresence);

        restPresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.getMatriculeAdherant()).isEqualTo(UPDATED_MATRICULE_ADHERANT);
        assertThat(testPresence.getStatutPresence()).isEqualTo(UPDATED_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void putNonExistingPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresenceWithPatch() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();

        // Update the presence using partial update
        Presence partialUpdatedPresence = new Presence();
        partialUpdatedPresence.setId(presence.getId());

        restPresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresence))
            )
            .andExpect(status().isOk());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.getMatriculeAdherant()).isEqualTo(DEFAULT_MATRICULE_ADHERANT);
        assertThat(testPresence.getStatutPresence()).isEqualTo(DEFAULT_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void fullUpdatePresenceWithPatch() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();

        // Update the presence using partial update
        Presence partialUpdatedPresence = new Presence();
        partialUpdatedPresence.setId(presence.getId());

        partialUpdatedPresence.matriculeAdherant(UPDATED_MATRICULE_ADHERANT).statutPresence(UPDATED_STATUT_PRESENCE);

        restPresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresence))
            )
            .andExpect(status().isOk());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.getMatriculeAdherant()).isEqualTo(UPDATED_MATRICULE_ADHERANT);
        assertThat(testPresence.getStatutPresence()).isEqualTo(UPDATED_STATUT_PRESENCE);
    }

    @Test
    @Transactional
    void patchNonExistingPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();
        presence.setId(longCount.incrementAndGet());

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(presenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeDelete = presenceRepository.findAll().size();

        // Delete the presence
        restPresenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, presence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
