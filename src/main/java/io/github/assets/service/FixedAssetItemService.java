package io.github.assets.service;

import io.github.assets.service.dto.FixedAssetItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.assets.domain.FixedAssetItem}.
 */
public interface FixedAssetItemService {

    /**
     * Save a fixedAssetItem.
     *
     * @param fixedAssetItemDTO the entity to save.
     * @return the persisted entity.
     */
    FixedAssetItemDTO save(FixedAssetItemDTO fixedAssetItemDTO);

    /**
     * Get all the fixedAssetItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FixedAssetItemDTO> findOne(Long id);

    /**
     * Delete the "id" fixedAssetItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fixedAssetItem corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetItemDTO> search(String query, Pageable pageable);
}
