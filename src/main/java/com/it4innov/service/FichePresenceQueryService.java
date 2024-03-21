package com.it4innov.service;

import com.it4innov.domain.*; // for static metamodels
import com.it4innov.domain.FichePresence;
import com.it4innov.repository.FichePresenceRepository;
import com.it4innov.service.criteria.FichePresenceCriteria;
import com.it4innov.service.dto.FichePresenceDTO;
import com.it4innov.service.mapper.FichePresenceMapper;
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
 * Service for executing complex queries for {@link FichePresence} entities in the database.
 * The main input is a {@link FichePresenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FichePresenceDTO} or a {@link Page} of {@link FichePresenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FichePresenceQueryService extends QueryService<FichePresence> {

    private final Logger log = LoggerFactory.getLogger(FichePresenceQueryService.class);

    private final FichePresenceRepository fichePresenceRepository;

    private final FichePresenceMapper fichePresenceMapper;

    public FichePresenceQueryService(FichePresenceRepository fichePresenceRepository, FichePresenceMapper fichePresenceMapper) {
        this.fichePresenceRepository = fichePresenceRepository;
        this.fichePresenceMapper = fichePresenceMapper;
    }

    /**
     * Return a {@link List} of {@link FichePresenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FichePresenceDTO> findByCriteria(FichePresenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceMapper.toDto(fichePresenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FichePresenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FichePresenceDTO> findByCriteria(FichePresenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceRepository.findAll(specification, page).map(fichePresenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FichePresenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceRepository.count(specification);
    }

    /**
     * Function to convert {@link FichePresenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FichePresence> createSpecification(FichePresenceCriteria criteria) {
        Specification<FichePresence> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FichePresence_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), FichePresence_.libelle));
            }
            if (criteria.getDateJour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateJour(), FichePresence_.dateJour));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FichePresence_.description));
            }
            if (criteria.getCodeEvenement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodeEvenement(), FichePresence_.codeEvenement));
            }
            if (criteria.getCodeTypeEvenement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCodeTypeEvenement(), FichePresence_.codeTypeEvenement));
            }
            if (criteria.getPresenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPresenceId(),
                            root -> root.join(FichePresence_.presences, JoinType.LEFT).get(Presence_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
