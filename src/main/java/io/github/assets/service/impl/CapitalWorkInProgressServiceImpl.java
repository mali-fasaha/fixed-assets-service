package io.github.assets.service.impl;

import io.github.assets.service.CapitalWorkInProgressService;
import io.github.assets.domain.CapitalWorkInProgress;
import io.github.assets.repository.CapitalWorkInProgressRepository;
import io.github.assets.service.dto.CapitalWorkInProgressDTO;
import io.github.assets.service.mapper.CapitalWorkInProgressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CapitalWorkInProgress}.
 */
@Service
@Transactional
public class CapitalWorkInProgressServiceImpl implements CapitalWorkInProgressService {

    private final Logger log = LoggerFactory.getLogger(CapitalWorkInProgressServiceImpl.class);

    private final CapitalWorkInProgressRepository capitalWorkInProgressRepository;

    private final CapitalWorkInProgressMapper capitalWorkInProgressMapper;

    public CapitalWorkInProgressServiceImpl(CapitalWorkInProgressRepository capitalWorkInProgressRepository, CapitalWorkInProgressMapper capitalWorkInProgressMapper) {
        this.capitalWorkInProgressRepository = capitalWorkInProgressRepository;
        this.capitalWorkInProgressMapper = capitalWorkInProgressMapper;
    }

    /**
     * Save a capitalWorkInProgress.
     *
     * @param capitalWorkInProgressDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CapitalWorkInProgressDTO save(CapitalWorkInProgressDTO capitalWorkInProgressDTO) {
        log.debug("Request to save CapitalWorkInProgress : {}", capitalWorkInProgressDTO);
        CapitalWorkInProgress capitalWorkInProgress = capitalWorkInProgressMapper.toEntity(capitalWorkInProgressDTO);
        capitalWorkInProgress = capitalWorkInProgressRepository.save(capitalWorkInProgress);
        return capitalWorkInProgressMapper.toDto(capitalWorkInProgress);
    }

    /**
     * Get all the capitalWorkInProgresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CapitalWorkInProgressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CapitalWorkInProgresses");
        return capitalWorkInProgressRepository.findAll(pageable)
            .map(capitalWorkInProgressMapper::toDto);
    }


    /**
     * Get one capitalWorkInProgress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CapitalWorkInProgressDTO> findOne(Long id) {
        log.debug("Request to get CapitalWorkInProgress : {}", id);
        return capitalWorkInProgressRepository.findById(id)
            .map(capitalWorkInProgressMapper::toDto);
    }

    /**
     * Delete the capitalWorkInProgress by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CapitalWorkInProgress : {}", id);
        capitalWorkInProgressRepository.deleteById(id);
    }
}
