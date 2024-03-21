package com.it4innov.service;

import com.it4innov.service.dto.FichePresenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.it4innov.domain.FichePresence}.
 */
public interface FichePresenceService {
    /**
     * Save a fichePresence.
     *
     * @param fichePresenceDTO the entity to save.
     * @return the persisted entity.
     */
    FichePresenceDTO save(FichePresenceDTO fichePresenceDTO);

    /**
     * Updates a fichePresence.
     *
     * @param fichePresenceDTO the entity to update.
     * @return the persisted entity.
     */
    FichePresenceDTO update(FichePresenceDTO fichePresenceDTO);

    /**
     * Partially updates a fichePresence.
     *
     * @param fichePresenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FichePresenceDTO> partialUpdate(FichePresenceDTO fichePresenceDTO);

    /**
     * Get all the fichePresences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FichePresenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fichePresence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FichePresenceDTO> findOne(Long id);

    /**
     * Delete the "id" fichePresence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
