package com.it4innov.repository;

import com.it4innov.domain.FichePresence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FichePresence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FichePresenceRepository extends JpaRepository<FichePresence, Long>, JpaSpecificationExecutor<FichePresence> {}
