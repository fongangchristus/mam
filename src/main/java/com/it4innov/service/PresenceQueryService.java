package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.Presence;
import com.it4innov.repository.PresenceRepository;
import com.it4innov.service.criteria.PresenceCriteria;
import com.it4innov.service.dto.PresenceDTO;
import com.it4innov.service.mapper.PresenceMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Presence} entities in the database.
 * The main input is a {@link PresenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PresenceDTO} or a {@link Page} of {@link PresenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PresenceQueryService extends QueryService<Presence> {

    private final Logger log = LoggerFactory.getLogger(PresenceQueryService.class);

    private final PresenceRepository presenceRepository;

    private final PresenceMapper presenceMapper;

    public PresenceQueryService(PresenceRepository presenceRepository, PresenceMapper presenceMapper) {
        this.presenceRepository = presenceRepository;
        this.presenceMapper = presenceMapper;
    }

    /**
     * Return a {@link List} of {@link PresenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PresenceDTO> findByCriteria(PresenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Presence> specification = createSpecification(criteria);
        return presenceMapper.toDto(presenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PresenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PresenceDTO> findByCriteria(PresenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Presence> specification = createSpecification(criteria);
        return presenceRepository.findAll(specification, page).map(presenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PresenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Presence> specification = createSpecification(criteria);
        return presenceRepository.count(specification);
    }

    /**
     * Function to convert {@link PresenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Presence> createSpecification(PresenceCriteria criteria) {
        Specification<Presence> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Presence_.id));
            }
            if (criteria.getMatriculeAdherant() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatriculeAdherant(), Presence_.matriculeAdherant));
            }
            if (criteria.getStatutPresence() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutPresence(), Presence_.statutPresence));
            }
            if (criteria.getFichePresenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFichePresenceId(),
                            root -> root.join(Presence_.fichePresence, JoinType.LEFT).get(FichePresence_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
