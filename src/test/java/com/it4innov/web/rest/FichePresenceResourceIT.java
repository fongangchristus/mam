package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.FichePresence;
import com.it4innov.domain.Presence;
import com.it4innov.repository.FichePresenceRepository;
import com.it4innov.service.dto.FichePresenceDTO;
import com.it4innov.service.mapper.FichePresenceMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link FichePresenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FichePresenceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_JOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_JOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_CODE_EVENEMENT = 1L;
    private static final Long UPDATED_CODE_EVENEMENT = 2L;
    private static final Long SMALLER_CODE_EVENEMENT = 1L - 1L;

    private static final Long DEFAULT_CODE_TYPE_EVENEMENT = 1L;
    private static final Long UPDATED_CODE_TYPE_EVENEMENT = 2L;
    private static final Long SMALLER_CODE_TYPE_EVENEMENT = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/fiche-presences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FichePresenceRepository fichePresenceRepository;

    @Autowired
    private FichePresenceMapper fichePresenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFichePresenceMockMvc;

    private FichePresence fichePresence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichePresence createEntity(EntityManager em) {
        FichePresence fichePresence = new FichePresence()
            .libelle(DEFAULT_LIBELLE)
            .dateJour(DEFAULT_DATE_JOUR)
            .description(DEFAULT_DESCRIPTION)
            .codeEvenement(DEFAULT_CODE_EVENEMENT)
            .codeTypeEvenement(DEFAULT_CODE_TYPE_EVENEMENT);
        return fichePresence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichePresence createUpdatedEntity(EntityManager em) {
        FichePresence fichePresence = new FichePresence()
            .libelle(UPDATED_LIBELLE)
            .dateJour(UPDATED_DATE_JOUR)
            .description(UPDATED_DESCRIPTION)
            .codeEvenement(UPDATED_CODE_EVENEMENT)
            .codeTypeEvenement(UPDATED_CODE_TYPE_EVENEMENT);
        return fichePresence;
    }

    @BeforeEach
    public void initTest() {
        fichePresence = createEntity(em);
    }

    @Test
    @Transactional
    void createFichePresence() throws Exception {
        int databaseSizeBeforeCreate = fichePresenceRepository.findAll().size();
        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);
        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeCreate + 1);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testFichePresence.getDateJour()).isEqualTo(DEFAULT_DATE_JOUR);
        assertThat(testFichePresence.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFichePresence.getCodeEvenement()).isEqualTo(DEFAULT_CODE_EVENEMENT);
        assertThat(testFichePresence.getCodeTypeEvenement()).isEqualTo(DEFAULT_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void createFichePresenceWithExistingId() throws Exception {
        // Create the FichePresence with an existing ID
        fichePresence.setId(1L);
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        int databaseSizeBeforeCreate = fichePresenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fichePresenceRepository.findAll().size();
        // set the field null
        fichePresence.setLibelle(null);

        // Create the FichePresence, which fails.
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateJourIsRequired() throws Exception {
        int databaseSizeBeforeTest = fichePresenceRepository.findAll().size();
        // set the field null
        fichePresence.setDateJour(null);

        // Create the FichePresence, which fails.
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeEvenementIsRequired() throws Exception {
        int databaseSizeBeforeTest = fichePresenceRepository.findAll().size();
        // set the field null
        fichePresence.setCodeEvenement(null);

        // Create the FichePresence, which fails.
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeTypeEvenementIsRequired() throws Exception {
        int databaseSizeBeforeTest = fichePresenceRepository.findAll().size();
        // set the field null
        fichePresence.setCodeTypeEvenement(null);

        // Create the FichePresence, which fails.
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        restFichePresenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFichePresences() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichePresence.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateJour").value(hasItem(DEFAULT_DATE_JOUR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].codeEvenement").value(hasItem(DEFAULT_CODE_EVENEMENT.intValue())))
            .andExpect(jsonPath("$.[*].codeTypeEvenement").value(hasItem(DEFAULT_CODE_TYPE_EVENEMENT.intValue())));
    }

    @Test
    @Transactional
    void getFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get the fichePresence
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL_ID, fichePresence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fichePresence.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateJour").value(DEFAULT_DATE_JOUR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.codeEvenement").value(DEFAULT_CODE_EVENEMENT.intValue()))
            .andExpect(jsonPath("$.codeTypeEvenement").value(DEFAULT_CODE_TYPE_EVENEMENT.intValue()));
    }

    @Test
    @Transactional
    void getFichePresencesByIdFiltering() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        Long id = fichePresence.getId();

        defaultFichePresenceShouldBeFound("id.equals=" + id);
        defaultFichePresenceShouldNotBeFound("id.notEquals=" + id);

        defaultFichePresenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFichePresenceShouldNotBeFound("id.greaterThan=" + id);

        defaultFichePresenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFichePresenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFichePresencesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where libelle equals to DEFAULT_LIBELLE
        defaultFichePresenceShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the fichePresenceList where libelle equals to UPDATED_LIBELLE
        defaultFichePresenceShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultFichePresenceShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the fichePresenceList where libelle equals to UPDATED_LIBELLE
        defaultFichePresenceShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where libelle is not null
        defaultFichePresenceShouldBeFound("libelle.specified=true");

        // Get all the fichePresenceList where libelle is null
        defaultFichePresenceShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where libelle contains DEFAULT_LIBELLE
        defaultFichePresenceShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the fichePresenceList where libelle contains UPDATED_LIBELLE
        defaultFichePresenceShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where libelle does not contain DEFAULT_LIBELLE
        defaultFichePresenceShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the fichePresenceList where libelle does not contain UPDATED_LIBELLE
        defaultFichePresenceShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateJourIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where dateJour equals to DEFAULT_DATE_JOUR
        defaultFichePresenceShouldBeFound("dateJour.equals=" + DEFAULT_DATE_JOUR);

        // Get all the fichePresenceList where dateJour equals to UPDATED_DATE_JOUR
        defaultFichePresenceShouldNotBeFound("dateJour.equals=" + UPDATED_DATE_JOUR);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateJourIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where dateJour in DEFAULT_DATE_JOUR or UPDATED_DATE_JOUR
        defaultFichePresenceShouldBeFound("dateJour.in=" + DEFAULT_DATE_JOUR + "," + UPDATED_DATE_JOUR);

        // Get all the fichePresenceList where dateJour equals to UPDATED_DATE_JOUR
        defaultFichePresenceShouldNotBeFound("dateJour.in=" + UPDATED_DATE_JOUR);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateJourIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where dateJour is not null
        defaultFichePresenceShouldBeFound("dateJour.specified=true");

        // Get all the fichePresenceList where dateJour is null
        defaultFichePresenceShouldNotBeFound("dateJour.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where description equals to DEFAULT_DESCRIPTION
        defaultFichePresenceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fichePresenceList where description equals to UPDATED_DESCRIPTION
        defaultFichePresenceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFichePresenceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fichePresenceList where description equals to UPDATED_DESCRIPTION
        defaultFichePresenceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where description is not null
        defaultFichePresenceShouldBeFound("description.specified=true");

        // Get all the fichePresenceList where description is null
        defaultFichePresenceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where description contains DEFAULT_DESCRIPTION
        defaultFichePresenceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fichePresenceList where description contains UPDATED_DESCRIPTION
        defaultFichePresenceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where description does not contain DEFAULT_DESCRIPTION
        defaultFichePresenceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fichePresenceList where description does not contain UPDATED_DESCRIPTION
        defaultFichePresenceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement equals to DEFAULT_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.equals=" + DEFAULT_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement equals to UPDATED_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.equals=" + UPDATED_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement in DEFAULT_CODE_EVENEMENT or UPDATED_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.in=" + DEFAULT_CODE_EVENEMENT + "," + UPDATED_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement equals to UPDATED_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.in=" + UPDATED_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement is not null
        defaultFichePresenceShouldBeFound("codeEvenement.specified=true");

        // Get all the fichePresenceList where codeEvenement is null
        defaultFichePresenceShouldNotBeFound("codeEvenement.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement is greater than or equal to DEFAULT_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.greaterThanOrEqual=" + DEFAULT_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement is greater than or equal to UPDATED_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.greaterThanOrEqual=" + UPDATED_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement is less than or equal to DEFAULT_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.lessThanOrEqual=" + DEFAULT_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement is less than or equal to SMALLER_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.lessThanOrEqual=" + SMALLER_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsLessThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement is less than DEFAULT_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.lessThan=" + DEFAULT_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement is less than UPDATED_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.lessThan=" + UPDATED_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeEvenementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeEvenement is greater than DEFAULT_CODE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeEvenement.greaterThan=" + DEFAULT_CODE_EVENEMENT);

        // Get all the fichePresenceList where codeEvenement is greater than SMALLER_CODE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeEvenement.greaterThan=" + SMALLER_CODE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement equals to DEFAULT_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.equals=" + DEFAULT_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement equals to UPDATED_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.equals=" + UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement in DEFAULT_CODE_TYPE_EVENEMENT or UPDATED_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.in=" + DEFAULT_CODE_TYPE_EVENEMENT + "," + UPDATED_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement equals to UPDATED_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.in=" + UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement is not null
        defaultFichePresenceShouldBeFound("codeTypeEvenement.specified=true");

        // Get all the fichePresenceList where codeTypeEvenement is null
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement is greater than or equal to DEFAULT_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.greaterThanOrEqual=" + DEFAULT_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement is greater than or equal to UPDATED_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.greaterThanOrEqual=" + UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement is less than or equal to DEFAULT_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.lessThanOrEqual=" + DEFAULT_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement is less than or equal to SMALLER_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.lessThanOrEqual=" + SMALLER_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsLessThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement is less than DEFAULT_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.lessThan=" + DEFAULT_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement is less than UPDATED_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.lessThan=" + UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByCodeTypeEvenementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where codeTypeEvenement is greater than DEFAULT_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldNotBeFound("codeTypeEvenement.greaterThan=" + DEFAULT_CODE_TYPE_EVENEMENT);

        // Get all the fichePresenceList where codeTypeEvenement is greater than SMALLER_CODE_TYPE_EVENEMENT
        defaultFichePresenceShouldBeFound("codeTypeEvenement.greaterThan=" + SMALLER_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByPresenceIsEqualToSomething() throws Exception {
        Presence presence;
        if (TestUtil.findAll(em, Presence.class).isEmpty()) {
            fichePresenceRepository.saveAndFlush(fichePresence);
            presence = PresenceResourceIT.createEntity(em);
        } else {
            presence = TestUtil.findAll(em, Presence.class).get(0);
        }
        em.persist(presence);
        em.flush();
        fichePresence.addPresence(presence);
        fichePresenceRepository.saveAndFlush(fichePresence);
        Long presenceId = presence.getId();
        // Get all the fichePresenceList where presence equals to presenceId
        defaultFichePresenceShouldBeFound("presenceId.equals=" + presenceId);

        // Get all the fichePresenceList where presence equals to (presenceId + 1)
        defaultFichePresenceShouldNotBeFound("presenceId.equals=" + (presenceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFichePresenceShouldBeFound(String filter) throws Exception {
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichePresence.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateJour").value(hasItem(DEFAULT_DATE_JOUR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].codeEvenement").value(hasItem(DEFAULT_CODE_EVENEMENT.intValue())))
            .andExpect(jsonPath("$.[*].codeTypeEvenement").value(hasItem(DEFAULT_CODE_TYPE_EVENEMENT.intValue())));

        // Check, that the count call also returns 1
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFichePresenceShouldNotBeFound(String filter) throws Exception {
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFichePresence() throws Exception {
        // Get the fichePresence
        restFichePresenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence
        FichePresence updatedFichePresence = fichePresenceRepository.findById(fichePresence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFichePresence are not directly saved in db
        em.detach(updatedFichePresence);
        updatedFichePresence
            .libelle(UPDATED_LIBELLE)
            .dateJour(UPDATED_DATE_JOUR)
            .description(UPDATED_DESCRIPTION)
            .codeEvenement(UPDATED_CODE_EVENEMENT)
            .codeTypeEvenement(UPDATED_CODE_TYPE_EVENEMENT);
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(updatedFichePresence);

        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichePresenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testFichePresence.getDateJour()).isEqualTo(UPDATED_DATE_JOUR);
        assertThat(testFichePresence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFichePresence.getCodeEvenement()).isEqualTo(UPDATED_CODE_EVENEMENT);
        assertThat(testFichePresence.getCodeTypeEvenement()).isEqualTo(UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void putNonExistingFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichePresenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFichePresenceWithPatch() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence using partial update
        FichePresence partialUpdatedFichePresence = new FichePresence();
        partialUpdatedFichePresence.setId(fichePresence.getId());

        partialUpdatedFichePresence.description(UPDATED_DESCRIPTION);

        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichePresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichePresence))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testFichePresence.getDateJour()).isEqualTo(DEFAULT_DATE_JOUR);
        assertThat(testFichePresence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFichePresence.getCodeEvenement()).isEqualTo(DEFAULT_CODE_EVENEMENT);
        assertThat(testFichePresence.getCodeTypeEvenement()).isEqualTo(DEFAULT_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void fullUpdateFichePresenceWithPatch() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence using partial update
        FichePresence partialUpdatedFichePresence = new FichePresence();
        partialUpdatedFichePresence.setId(fichePresence.getId());

        partialUpdatedFichePresence
            .libelle(UPDATED_LIBELLE)
            .dateJour(UPDATED_DATE_JOUR)
            .description(UPDATED_DESCRIPTION)
            .codeEvenement(UPDATED_CODE_EVENEMENT)
            .codeTypeEvenement(UPDATED_CODE_TYPE_EVENEMENT);

        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichePresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichePresence))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testFichePresence.getDateJour()).isEqualTo(UPDATED_DATE_JOUR);
        assertThat(testFichePresence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFichePresence.getCodeEvenement()).isEqualTo(UPDATED_CODE_EVENEMENT);
        assertThat(testFichePresence.getCodeTypeEvenement()).isEqualTo(UPDATED_CODE_TYPE_EVENEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fichePresenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(longCount.incrementAndGet());

        // Create the FichePresence
        FichePresenceDTO fichePresenceDTO = fichePresenceMapper.toDto(fichePresence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichePresenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeDelete = fichePresenceRepository.findAll().size();

        // Delete the fichePresence
        restFichePresenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, fichePresence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
