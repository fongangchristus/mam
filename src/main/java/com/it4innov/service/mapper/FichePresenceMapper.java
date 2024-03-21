package com.it4innov.service.mapper;

import com.it4innov.domain.FichePresence;
import com.it4innov.service.dto.FichePresenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FichePresence} and its DTO {@link FichePresenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface FichePresenceMapper extends EntityMapper<FichePresenceDTO, FichePresence> {}
