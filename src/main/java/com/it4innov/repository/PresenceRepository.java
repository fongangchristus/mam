package com.it4innov.repository;

import com.it4innov.domain.Presence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Presence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long>, JpaSpecificationExecutor<Presence> {}
