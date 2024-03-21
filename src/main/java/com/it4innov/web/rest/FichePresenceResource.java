package com.it4innov.web.rest;

import com.it4innov.repository.FichePresenceRepository;
import com.it4innov.service.FichePresenceQueryService;
import com.it4innov.service.FichePresenceService;
import com.it4innov.service.criteria.FichePresenceCriteria;
import com.it4innov.service.dto.FichePresenceDTO;
import com.it4innov.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.it4innov.domain.FichePresence}.
 */
@RestController
@RequestMapping("/api/fiche-presences")
public class FichePresenceResource {

    private final Logger log = LoggerFactory.getLogger(FichePresenceResource.class);

    private static final String ENTITY_NAME = "fichePresence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FichePresenceService fichePresenceService;

    private final FichePresenceRepository fichePresenceRepository;

    private final FichePresenceQueryService fichePresenceQueryService;

    public FichePresenceResource(
        FichePresenceService fichePresenceService,
        FichePresenceRepository fichePresenceRepository,
        FichePresenceQueryService fichePresenceQueryService
    ) {
        this.fichePresenceService = fichePresenceService;
        this.fichePresenceRepository = fichePresenceRepository;
        this.fichePresenceQueryService = fichePresenceQueryService;
    }

    /**
     * {@code POST  /fiche-presences} : Create a new fichePresence.
     *
     * @param fichePresenceDTO the fichePresenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fichePresenceDTO, or with status {@code 400 (Bad Request)} if the fichePresence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FichePresenceDTO> createFichePresence(@Valid @RequestBody FichePresenceDTO fichePresenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save FichePresence : {}", fichePresenceDTO);
        if (fichePresenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new fichePresence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FichePresenceDTO result = fichePresenceService.save(fichePresenceDTO);
        return ResponseEntity
            .created(new URI("/api/fiche-presences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiche-presences/:id} : Updates an existing fichePresence.
     *
     * @param id the id of the fichePresenceDTO to save.
     * @param fichePresenceDTO the fichePresenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichePresenceDTO,
     * or with status {@code 400 (Bad Request)} if the fichePresenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fichePresenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FichePresenceDTO> updateFichePresence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FichePresenceDTO fichePresenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FichePresence : {}, {}", id, fichePresenceDTO);
        if (fichePresenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichePresenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichePresenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FichePresenceDTO result = fichePresenceService.update(fichePresenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fichePresenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiche-presences/:id} : Partial updates given fields of an existing fichePresence, field will ignore if it is null
     *
     * @param id the id of the fichePresenceDTO to save.
     * @param fichePresenceDTO the fichePresenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichePresenceDTO,
     * or with status {@code 400 (Bad Request)} if the fichePresenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fichePresenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fichePresenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FichePresenceDTO> partialUpdateFichePresence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FichePresenceDTO fichePresenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FichePresence partially : {}, {}", id, fichePresenceDTO);
        if (fichePresenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichePresenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichePresenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FichePresenceDTO> result = fichePresenceService.partialUpdate(fichePresenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fichePresenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiche-presences} : get all the fichePresences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fichePresences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FichePresenceDTO>> getAllFichePresences(
        FichePresenceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FichePresences by criteria: {}", criteria);

        Page<FichePresenceDTO> page = fichePresenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiche-presences/count} : count all the fichePresences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFichePresences(FichePresenceCriteria criteria) {
        log.debug("REST request to count FichePresences by criteria: {}", criteria);
        return ResponseEntity.ok().body(fichePresenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiche-presences/:id} : get the "id" fichePresence.
     *
     * @param id the id of the fichePresenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fichePresenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FichePresenceDTO> getFichePresence(@PathVariable("id") Long id) {
        log.debug("REST request to get FichePresence : {}", id);
        Optional<FichePresenceDTO> fichePresenceDTO = fichePresenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fichePresenceDTO);
    }

    /**
     * {@code DELETE  /fiche-presences/:id} : delete the "id" fichePresence.
     *
     * @param id the id of the fichePresenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFichePresence(@PathVariable("id") Long id) {
        log.debug("REST request to delete FichePresence : {}", id);
        fichePresenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
