package io.github.assets.service.impl;

import io.github.assets.service.AssetTransactionService;
import io.github.assets.domain.AssetTransaction;
import io.github.assets.repository.AssetTransactionRepository;
import io.github.assets.service.dto.AssetTransactionDTO;
import io.github.assets.service.mapper.AssetTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AssetTransaction}.
 */
@Service
@Transactional
public class AssetTransactionServiceImpl implements AssetTransactionService {

    private final Logger log = LoggerFactory.getLogger(AssetTransactionServiceImpl.class);

    private final AssetTransactionRepository assetTransactionRepository;

    private final AssetTransactionMapper assetTransactionMapper;

    public AssetTransactionServiceImpl(AssetTransactionRepository assetTransactionRepository, AssetTransactionMapper assetTransactionMapper) {
        this.assetTransactionRepository = assetTransactionRepository;
        this.assetTransactionMapper = assetTransactionMapper;
    }

    /**
     * Save a assetTransaction.
     *
     * @param assetTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AssetTransactionDTO save(AssetTransactionDTO assetTransactionDTO) {
        log.debug("Request to save AssetTransaction : {}", assetTransactionDTO);
        AssetTransaction assetTransaction = assetTransactionMapper.toEntity(assetTransactionDTO);
        assetTransaction = assetTransactionRepository.save(assetTransaction);
        return assetTransactionMapper.toDto(assetTransaction);
    }

    /**
     * Get all the assetTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AssetTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetTransactions");
        return assetTransactionRepository.findAll(pageable)
            .map(assetTransactionMapper::toDto);
    }


    /**
     * Get one assetTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AssetTransactionDTO> findOne(Long id) {
        log.debug("Request to get AssetTransaction : {}", id);
        return assetTransactionRepository.findById(id)
            .map(assetTransactionMapper::toDto);
    }

    /**
     * Delete the assetTransaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetTransaction : {}", id);
        assetTransactionRepository.deleteById(id);
    }
}
