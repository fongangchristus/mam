package com.it4innov.service.impl;

import com.it4innov.domain.FichePresence;
import com.it4innov.repository.FichePresenceRepository;
import com.it4innov.service.FichePresenceService;
import com.it4innov.service.dto.FichePresenceDTO;
import com.it4innov.service.mapper.FichePresenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.it4innov.domain.FichePresence}.
 */
@Service
@Transactional
public class FichePresenceServiceImpl implements FichePresenceService {

    private final Logger log = LoggerFactory.getLogger(FichePresenceServiceImpl.class);

    private final FichePresenceRepository fichePresenceRepository;

    private final FichePresenceMapper fichePresenceMapper;

    public FichePresenceServiceImpl(FichePresenceRepository fichePresenceRepository, FichePresenceMapper fichePresenceMapper) {
        this.fichePresenceRepository = fichePresenceRepository;
        this.fichePresenceMapper = fichePresenceMapper;
    }

    @Override
    public FichePresenceDTO save(FichePresenceDTO fichePresenceDTO) {
        log.debug("Request to save FichePresence : {}", fichePresenceDTO);
        FichePresence fichePresence = fichePresenceMapper.toEntity(fichePresenceDTO);
        fichePresence = fichePresenceRepository.save(fichePresence);
        return fichePresenceMapper.toDto(fichePresence);
    }

    @Override
    public FichePresenceDTO update(FichePresenceDTO fichePresenceDTO) {
        log.debug("Request to update FichePresence : {}", fichePresenceDTO);
        FichePresence fichePresence = fichePresenceMapper.toEntity(fichePresenceDTO);
        fichePresence = fichePresenceRepository.save(fichePresence);
        return fichePresenceMapper.toDto(fichePresence);
    }

    @Override
    public Optional<FichePresenceDTO> partialUpdate(FichePresenceDTO fichePresenceDTO) {
        log.debug("Request to partially update FichePresence : {}", fichePresenceDTO);

        return fichePresenceRepository
            .findById(fichePresenceDTO.getId())
            .map(existingFichePresence -> {
                fichePresenceMapper.partialUpdate(existingFichePresence, fichePresenceDTO);

                return existingFichePresence;
            })
            .map(fichePresenceRepository::save)
            .map(fichePresenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FichePresenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FichePresences");
        return fichePresenceRepository.findAll(pageable).map(fichePresenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FichePresenceDTO> findOne(Long id) {
        log.debug("Request to get FichePresence : {}", id);
        return fichePresenceRepository.findById(id).map(fichePresenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FichePresence : {}", id);
        fichePresenceRepository.deleteById(id);
    }
}
