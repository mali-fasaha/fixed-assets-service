package io.github.assets.service.impl;

import io.github.assets.service.FixedAssetItemService;
import io.github.assets.domain.FixedAssetItem;
import io.github.assets.repository.FixedAssetItemRepository;
import io.github.assets.service.dto.FixedAssetItemDTO;
import io.github.assets.service.mapper.FixedAssetItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FixedAssetItem}.
 */
@Service
@Transactional
public class FixedAssetItemServiceImpl implements FixedAssetItemService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetItemServiceImpl.class);

    private final FixedAssetItemRepository fixedAssetItemRepository;

    private final FixedAssetItemMapper fixedAssetItemMapper;

    public FixedAssetItemServiceImpl(FixedAssetItemRepository fixedAssetItemRepository, FixedAssetItemMapper fixedAssetItemMapper) {
        this.fixedAssetItemRepository = fixedAssetItemRepository;
        this.fixedAssetItemMapper = fixedAssetItemMapper;
    }

    /**
     * Save a fixedAssetItem.
     *
     * @param fixedAssetItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FixedAssetItemDTO save(FixedAssetItemDTO fixedAssetItemDTO) {
        log.debug("Request to save FixedAssetItem : {}", fixedAssetItemDTO);
        FixedAssetItem fixedAssetItem = fixedAssetItemMapper.toEntity(fixedAssetItemDTO);
        fixedAssetItem = fixedAssetItemRepository.save(fixedAssetItem);
        return fixedAssetItemMapper.toDto(fixedAssetItem);
    }

    /**
     * Get all the fixedAssetItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetItems");
        return fixedAssetItemRepository.findAll(pageable)
            .map(fixedAssetItemMapper::toDto);
    }


    /**
     * Get one fixedAssetItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetItemDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetItem : {}", id);
        return fixedAssetItemRepository.findById(id)
            .map(fixedAssetItemMapper::toDto);
    }

    /**
     * Delete the fixedAssetItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetItem : {}", id);
        fixedAssetItemRepository.deleteById(id);
    }
}
