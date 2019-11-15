package io.github.assets.service.impl;

import io.github.assets.service.FixedAssetInvoiceService;
import io.github.assets.domain.FixedAssetInvoice;
import io.github.assets.repository.FixedAssetInvoiceRepository;
import io.github.assets.service.dto.FixedAssetInvoiceDTO;
import io.github.assets.service.mapper.FixedAssetInvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FixedAssetInvoice}.
 */
@Service
@Transactional
public class FixedAssetInvoiceServiceImpl implements FixedAssetInvoiceService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetInvoiceServiceImpl.class);

    private final FixedAssetInvoiceRepository fixedAssetInvoiceRepository;

    private final FixedAssetInvoiceMapper fixedAssetInvoiceMapper;

    public FixedAssetInvoiceServiceImpl(FixedAssetInvoiceRepository fixedAssetInvoiceRepository, FixedAssetInvoiceMapper fixedAssetInvoiceMapper) {
        this.fixedAssetInvoiceRepository = fixedAssetInvoiceRepository;
        this.fixedAssetInvoiceMapper = fixedAssetInvoiceMapper;
    }

    /**
     * Save a fixedAssetInvoice.
     *
     * @param fixedAssetInvoiceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FixedAssetInvoiceDTO save(FixedAssetInvoiceDTO fixedAssetInvoiceDTO) {
        log.debug("Request to save FixedAssetInvoice : {}", fixedAssetInvoiceDTO);
        FixedAssetInvoice fixedAssetInvoice = fixedAssetInvoiceMapper.toEntity(fixedAssetInvoiceDTO);
        fixedAssetInvoice = fixedAssetInvoiceRepository.save(fixedAssetInvoice);
        return fixedAssetInvoiceMapper.toDto(fixedAssetInvoice);
    }

    /**
     * Get all the fixedAssetInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetInvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetInvoices");
        return fixedAssetInvoiceRepository.findAll(pageable)
            .map(fixedAssetInvoiceMapper::toDto);
    }


    /**
     * Get one fixedAssetInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetInvoiceDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetInvoice : {}", id);
        return fixedAssetInvoiceRepository.findById(id)
            .map(fixedAssetInvoiceMapper::toDto);
    }

    /**
     * Delete the fixedAssetInvoice by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetInvoice : {}", id);
        fixedAssetInvoiceRepository.deleteById(id);
    }
}
