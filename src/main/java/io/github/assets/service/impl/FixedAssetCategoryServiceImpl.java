package io.github.assets.service.impl;

import io.github.assets.service.FixedAssetCategoryService;
import io.github.assets.domain.FixedAssetCategory;
import io.github.assets.repository.FixedAssetCategoryRepository;
import io.github.assets.service.dto.FixedAssetCategoryDTO;
import io.github.assets.service.mapper.FixedAssetCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FixedAssetCategory}.
 */
@Service
@Transactional
public class FixedAssetCategoryServiceImpl implements FixedAssetCategoryService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetCategoryServiceImpl.class);

    private final FixedAssetCategoryRepository fixedAssetCategoryRepository;

    private final FixedAssetCategoryMapper fixedAssetCategoryMapper;

    public FixedAssetCategoryServiceImpl(FixedAssetCategoryRepository fixedAssetCategoryRepository, FixedAssetCategoryMapper fixedAssetCategoryMapper) {
        this.fixedAssetCategoryRepository = fixedAssetCategoryRepository;
        this.fixedAssetCategoryMapper = fixedAssetCategoryMapper;
    }

    /**
     * Save a fixedAssetCategory.
     *
     * @param fixedAssetCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FixedAssetCategoryDTO save(FixedAssetCategoryDTO fixedAssetCategoryDTO) {
        log.debug("Request to save FixedAssetCategory : {}", fixedAssetCategoryDTO);
        FixedAssetCategory fixedAssetCategory = fixedAssetCategoryMapper.toEntity(fixedAssetCategoryDTO);
        fixedAssetCategory = fixedAssetCategoryRepository.save(fixedAssetCategory);
        return fixedAssetCategoryMapper.toDto(fixedAssetCategory);
    }

    /**
     * Get all the fixedAssetCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetCategories");
        return fixedAssetCategoryRepository.findAll(pageable)
            .map(fixedAssetCategoryMapper::toDto);
    }


    /**
     * Get one fixedAssetCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetCategoryDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetCategory : {}", id);
        return fixedAssetCategoryRepository.findById(id)
            .map(fixedAssetCategoryMapper::toDto);
    }

    /**
     * Delete the fixedAssetCategory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetCategory : {}", id);
        fixedAssetCategoryRepository.deleteById(id);
    }
}
