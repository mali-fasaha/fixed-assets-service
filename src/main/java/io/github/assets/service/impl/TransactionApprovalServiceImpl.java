package io.github.assets.service.impl;

import io.github.assets.service.TransactionApprovalService;
import io.github.assets.domain.TransactionApproval;
import io.github.assets.repository.TransactionApprovalRepository;
import io.github.assets.service.dto.TransactionApprovalDTO;
import io.github.assets.service.mapper.TransactionApprovalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionApproval}.
 */
@Service
@Transactional
public class TransactionApprovalServiceImpl implements TransactionApprovalService {

    private final Logger log = LoggerFactory.getLogger(TransactionApprovalServiceImpl.class);

    private final TransactionApprovalRepository transactionApprovalRepository;

    private final TransactionApprovalMapper transactionApprovalMapper;

    public TransactionApprovalServiceImpl(TransactionApprovalRepository transactionApprovalRepository, TransactionApprovalMapper transactionApprovalMapper) {
        this.transactionApprovalRepository = transactionApprovalRepository;
        this.transactionApprovalMapper = transactionApprovalMapper;
    }

    /**
     * Save a transactionApproval.
     *
     * @param transactionApprovalDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionApprovalDTO save(TransactionApprovalDTO transactionApprovalDTO) {
        log.debug("Request to save TransactionApproval : {}", transactionApprovalDTO);
        TransactionApproval transactionApproval = transactionApprovalMapper.toEntity(transactionApprovalDTO);
        transactionApproval = transactionApprovalRepository.save(transactionApproval);
        return transactionApprovalMapper.toDto(transactionApproval);
    }

    /**
     * Get all the transactionApprovals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionApprovalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionApprovals");
        return transactionApprovalRepository.findAll(pageable)
            .map(transactionApprovalMapper::toDto);
    }


    /**
     * Get one transactionApproval by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionApprovalDTO> findOne(Long id) {
        log.debug("Request to get TransactionApproval : {}", id);
        return transactionApprovalRepository.findById(id)
            .map(transactionApprovalMapper::toDto);
    }

    /**
     * Delete the transactionApproval by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionApproval : {}", id);
        transactionApprovalRepository.deleteById(id);
    }
}
