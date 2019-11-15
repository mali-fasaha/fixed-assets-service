package io.github.assets.service.impl;

import io.github.assets.service.AssetDepreciationService;
import io.github.assets.domain.AssetDepreciation;
import io.github.assets.repository.AssetDepreciationRepository;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.mapper.AssetDepreciationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AssetDepreciation}.
 */
@Service
@Transactional
public class AssetDepreciationServiceImpl implements AssetDepreciationService {

    private final Logger log = LoggerFactory.getLogger(AssetDepreciationServiceImpl.class);

    private final AssetDepreciationRepository assetDepreciationRepository;

    private final AssetDepreciationMapper assetDepreciationMapper;

    public AssetDepreciationServiceImpl(AssetDepreciationRepository assetDepreciationRepository, AssetDepreciationMapper assetDepreciationMapper) {
        this.assetDepreciationRepository = assetDepreciationRepository;
        this.assetDepreciationMapper = assetDepreciationMapper;
    }

    /**
     * Save a assetDepreciation.
     *
     * @param assetDepreciationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AssetDepreciationDTO save(AssetDepreciationDTO assetDepreciationDTO) {
        log.debug("Request to save AssetDepreciation : {}", assetDepreciationDTO);
        AssetDepreciation assetDepreciation = assetDepreciationMapper.toEntity(assetDepreciationDTO);
        assetDepreciation = assetDepreciationRepository.save(assetDepreciation);
        return assetDepreciationMapper.toDto(assetDepreciation);
    }

    /**
     * Get all the assetDepreciations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AssetDepreciationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDepreciations");
        return assetDepreciationRepository.findAll(pageable)
            .map(assetDepreciationMapper::toDto);
    }


    /**
     * Get one assetDepreciation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDepreciationDTO> findOne(Long id) {
        log.debug("Request to get AssetDepreciation : {}", id);
        return assetDepreciationRepository.findById(id)
            .map(assetDepreciationMapper::toDto);
    }

    /**
     * Delete the assetDepreciation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDepreciation : {}", id);
        assetDepreciationRepository.deleteById(id);
    }
}
